package com.sea.service;

import com.sea.model.Exam;
import com.sea.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public List<Exam> getActiveExams() {
        return examRepository.findByIsActiveTrue();
    }

    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with ID: " + id));
    }

    public Exam getExamByCode(String code) {
        return examRepository.findByExamCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with code: " + code));
    }

    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }

    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    public long getCount() {
        return examRepository.count();
    }
}
