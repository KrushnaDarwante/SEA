package com.sea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {
    private Long id;
    private String rollNo;
    private String studentName;
    private String department;
    private String seatNumber;
    private String hallNumber;
    private String examCode;
    private String status;
    private LocalDateTime scanTime;
    private String verifiedBy;
    private String remarks;
}
