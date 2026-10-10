package com.sea.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QRScanRequest {
    @NotBlank(message = "QR Payload string is required")
    private String qrPayload;

    private Long examId;

    private String teacherEmployeeId;

    private String deviceMeta;
}
