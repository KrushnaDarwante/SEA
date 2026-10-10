package com.sea.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "qr_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_payload", nullable = false, length = 255)
    private String qrPayload;

    @Column(name = "verification_hash", nullable = false, length = 64)
    private String verificationHash;

    @Column(name = "student_roll", nullable = false, length = 32)
    private String studentRoll;

    @Column(name = "exam_code", nullable = false, length = 32)
    private String examCode;

    @Column(name = "is_used", nullable = false)
    @Builder.Default
    private Boolean isUsed = false;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "device_ip", length = 45)
    private String deviceIp;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
