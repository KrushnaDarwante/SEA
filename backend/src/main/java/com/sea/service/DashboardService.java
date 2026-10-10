package com.sea.service;

import com.sea.dto.DashboardStatsDTO;
import com.sea.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final HallRepository hallRepository;
    private final ExamRepository examRepository;
    private final SeatAssignmentRepository seatAssignmentRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardStatsDTO getStats() {
        long students = studentRepository.count();
        long teachers = teacherRepository.count();
        long halls = hallRepository.count();
        long exams = examRepository.count();
        long allotted = seatAssignmentRepository.count();
        long present = attendanceRepository.count();

        double percentage = allotted > 0 ? ((double) present / allotted) * 100.0 : 0.0;
        percentage = Math.round(percentage * 10.0) / 10.0;

        return DashboardStatsDTO.builder()
                .totalStudents(students)
                .totalTeachers(teachers)
                .totalHalls(halls)
                .totalExams(exams)
                .totalSeatsAllotted(allotted)
                .totalPresent(present)
                .attendancePercentage(percentage)
                .build();
    }
}
