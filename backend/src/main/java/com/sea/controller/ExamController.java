package com.sea.controller;

import com.sea.dto.ApiResponse;
import com.sea.model.Exam;
import com.sea.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamController {

    private final ExamService examService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Exam>>> getAllExams() {
        return ResponseEntity.ok(ApiResponse.ok(examService.getAllExams()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Exam>> getExamById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(examService.getExamById(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Exam>> createExam(@RequestBody Exam exam) {
        try {
            Exam saved = examService.saveExam(exam);
            return ResponseEntity.ok(ApiResponse.ok("Exam created successfully", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create exam: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExam(@PathVariable Long id) {
        try {
            examService.deleteExam(id);
            return ResponseEntity.ok(ApiResponse.ok("Exam deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to delete exam: " + e.getMessage()));
        }
    }
}
