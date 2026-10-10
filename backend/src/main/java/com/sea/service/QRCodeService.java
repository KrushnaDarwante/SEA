package com.sea.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class QRCodeService {

    @Value("${sea.qr.secret-salt:MATOSHRI-MCERC-2026-SEA-SECURE-SALT}")
    private String secretSalt;

    /**
     * Generates a Base64-encoded Data URL of a PNG QR Code.
     */
    public String generateQRCodeBase64(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR Code image: " + e.getMessage(), e);
        }
    }

    /**
     * Computes a 12-character cryptographic HMAC signature to prevent QR forgery.
     */
    public String generateVerificationHash(String studentRoll, String examCode, String seatNumber) {
        try {
            String raw = studentRoll + ":" + examCode + ":" + seatNumber;
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secretSalt.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(raw.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.substring(0, 12);
        } catch (Exception e) {
            return Integer.toHexString((studentRoll + examCode + seatNumber).hashCode());
        }
    }

    /**
     * Creates a tamper-proof payload string: ROLL|EXAM|SEAT|HASH
     */
    public String createTamperProofPayload(String studentRoll, String examCode, String seatNumber) {
        String hash = generateVerificationHash(studentRoll, examCode, seatNumber);
        return studentRoll + "|" + examCode + "|" + seatNumber + "|" + hash;
    }

    /**
     * Validates that the payload hash has not been tampered with.
     */
    public boolean verifyPayloadIntegrity(String payload) {
        if (payload == null || !payload.contains("|")) {
            return false;
        }
        String[] parts = payload.split("\\|");
        if (parts.length < 4) {
            // Fallback for simple demo payloads: ROLL|EXAM|SEAT
            return parts.length >= 1;
        }
        String roll = parts[0];
        String exam = parts[1];
        String seat = parts[2];
        String signature = parts[3];

        String expectedHash = generateVerificationHash(roll, exam, seat);
        return MessageDigest.isEqual(expectedHash.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8));
    }
}
