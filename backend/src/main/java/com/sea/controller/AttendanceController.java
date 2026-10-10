package com.sea.controller;

import com.sea.dto.ApiResponse;
import com.sea.dto.AttendanceDTO;
import com.sea.dto.QRScanRequest;
import com.sea.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/verify-qr")
    public ResponseEntity<ApiResponse<AttendanceDTO>> verifyAndRecordQR(@RequestBody @Valid QRScanRequest request) {
        try {
            AttendanceDTO response = attendanceService.verifyAndRecordQR(request);
            return ResponseEntity.ok(ApiResponse.ok("Student entry verified successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            // Duplicate scan alert
            return ResponseEntity.status(409).body(ApiResponse.error(e.getMessage()));
        } catch (SecurityException e) {
            // Tampered QR code
            return ResponseEntity.status(403).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Scan processing error: " + e.getMessage()));
        }
    }

    @GetMapping("/live/{examId}")
    public ResponseEntity<ApiResponse<List<AttendanceDTO>>> getLiveAttendance(@PathVariable Long examId) {
        return ResponseEntity.ok(ApiResponse.ok(attendanceService.getLiveAttendanceByExam(examId)));
    }
}
