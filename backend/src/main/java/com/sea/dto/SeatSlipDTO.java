package com.sea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatSlipDTO {
    private Long assignmentId;
    private String studentRoll;
    private String studentName;
    private String department;
    private Integer year;
    private String examCode;
    private String examTitle;
    private String subject;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String hallNumber;
    private String block;
    private String seatNumber; // R3-C5
    private Integer row;
    private Integer col;
    private String supervisorName;
    private String qrCodeData;
    private String qrCodeImage; // Base64 data URL
    private boolean verified;
}
