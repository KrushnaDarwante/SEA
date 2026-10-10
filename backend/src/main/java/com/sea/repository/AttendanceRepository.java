package com.sea.repository;

import com.sea.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByExamId(Long examId);
    Optional<Attendance> findBySeatAssignmentId(Long seatAssignmentId);
    Optional<Attendance> findByExamIdAndStudentId(Long examId, Long studentId);
    Optional<Attendance> findByExamIdAndStudentRollNo(Long examId, String rollNo);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.exam.id = :examId AND a.status = 'PRESENT'")
    long countPresentByExamId(Long examId);

    long countByExamId(Long examId);
}
