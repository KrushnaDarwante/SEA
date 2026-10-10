package com.sea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalStudents;
    private long totalTeachers;
    private long totalHalls;
    private long totalExams;
    private long totalSeatsAllotted;
    private long totalPresent;
    private double attendancePercentage;
}
