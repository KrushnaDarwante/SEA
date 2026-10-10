package com.sea.repository;

import com.sea.model.SeatAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatAssignmentRepository extends JpaRepository<SeatAssignment, Long> {
    List<SeatAssignment> findByExamId(Long examId);
    List<SeatAssignment> findByExamIdAndHallId(Long examId, Long hallId);
    Optional<SeatAssignment> findByExamIdAndStudentId(Long examId, Long studentId);
    Optional<SeatAssignment> findByExamIdAndStudentRollNo(Long examId, String rollNo);
    Optional<SeatAssignment> findByStudentRollNo(String rollNo);
    void deleteByExamId(Long examId);
    long countByExamId(Long examId);
}
