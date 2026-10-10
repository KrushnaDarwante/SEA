package com.sea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentResponse {
    private Long examId;
    private String examCode;
    private String examTitle;
    private int totalStudentsAllotted;
    private int totalHallsUsed;
    private long executionTimeMs;
    private List<SeatSlipDTO> allocations;
}
