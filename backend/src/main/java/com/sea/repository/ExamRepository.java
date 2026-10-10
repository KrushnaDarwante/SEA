package com.sea.repository;

import com.sea.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Optional<Exam> findByExamCode(String examCode);
    List<Exam> findByIsActiveTrue();
    List<Exam> findByExamDate(LocalDate date);
    Boolean existsByExamCode(String examCode);
}
