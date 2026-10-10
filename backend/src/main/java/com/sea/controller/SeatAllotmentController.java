package com.sea.controller;

import com.sea.dto.AllotmentRequest;
import com.sea.dto.AllotmentResponse;
import com.sea.dto.ApiResponse;
import com.sea.dto.SeatSlipDTO;
import com.sea.model.Attendance;
import com.sea.model.SeatAssignment;
import com.sea.repository.AttendanceRepository;
import com.sea.repository.SeatAssignmentRepository;
import com.sea.service.AntiMalpracticeSeatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/allotment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeatAllotmentController {

    private final AntiMalpracticeSeatingService seatingService;
    private final SeatAssignmentRepository seatAssignmentRepository;
    private final AttendanceRepository attendanceRepository;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<AllotmentResponse>> generateAllotment(@RequestBody @Valid AllotmentRequest request) {
        try {
            AllotmentResponse response = seatingService.generateSeating(request);
            return ResponseEntity.ok(ApiResponse.ok("One-click anti-malpractice seat allotment completed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Allotment failed: " + e.getMessage()));
        }
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<ApiResponse<List<SeatSlipDTO>>> getAssignmentsByExam(@PathVariable Long examId) {
        List<SeatAssignment> list = seatAssignmentRepository.findByExamId(examId);
        List<SeatSlipDTO> dtos = list.stream().map(this::mapToSlipDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(dtos));
    }

    @GetMapping("/student/{rollNo}")
    public ResponseEntity<ApiResponse<SeatSlipDTO>> getStudentSeatSlip(@PathVariable String rollNo) {
        Optional<SeatAssignment> assignmentOpt = seatAssignmentRepository.findByStudentRollNo(rollNo);
        if (assignmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("No seat allotment found for roll number: " + rollNo));
        }
        return ResponseEntity.ok(ApiResponse.ok(mapToSlipDTO(assignmentOpt.get())));
    }

    private SeatSlipDTO mapToSlipDTO(SeatAssignment sa) {
        boolean verified = attendanceRepository.findBySeatAssignmentId(sa.getId())
                .map(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                .orElse(false);

        return SeatSlipDTO.builder()
                .assignmentId(sa.getId())
                .studentRoll(sa.getStudent().getRollNo())
                .studentName(sa.getStudent().getName())
                .department(sa.getStudent().getDepartment())
                .year(sa.getStudent().getYear())
                .examCode(sa.getExam().getExamCode())
                .examTitle(sa.getExam().getTitle())
                .subject(sa.getExam().getSubject())
                .examDate(sa.getExam().getExamDate())
                .startTime(sa.getExam().getStartTime())
                .endTime(sa.getExam().getEndTime())
                .hallNumber(sa.getHall().getHallNumber())
                .block(sa.getHall().getBlock())
                .seatNumber(sa.getSeatNumber())
                .row(sa.getRowNumber())
                .col(sa.getColNumber())
                .supervisorName(sa.getSupervisor() != null ? sa.getSupervisor().getName() : "Prof. Invigilator")
                .qrCodeData(sa.getQrCodeData())
                .qrCodeImage(sa.getQrCodeImage())
                .verified(verified)
                .build();
    }
}
