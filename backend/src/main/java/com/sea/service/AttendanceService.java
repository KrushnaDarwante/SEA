package com.sea.service;

import com.sea.dto.AttendanceDTO;
import com.sea.dto.QRScanRequest;
import com.sea.model.*;
import com.sea.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final SeatAssignmentRepository seatAssignmentRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final TeacherRepository teacherRepository;
    private final QRVerificationRepository qrVerificationRepository;
    private final QRCodeService qrCodeService;

    @Transactional
    public AttendanceDTO verifyAndRecordQR(QRScanRequest request) {
        String payload = request.getQrPayload().trim();

        // 1. Parse QR payload: Format: ROLL|EXAM_CODE|SEAT|HASH or ROLL
        String rollNo;
        String examCode = null;

        if (payload.contains("|")) {
            String[] parts = payload.split("\\|");
            rollNo = parts[0].trim();
            if (parts.length > 1) examCode = parts[1].trim();

            // Verify tamper-proof hash if present
            if (parts.length >= 4 && !qrCodeService.verifyPayloadIntegrity(payload)) {
                throw new SecurityException("Tampered QR Code detected. Cryptographic verification signature failed.");
            }
        } else {
            rollNo = payload;
        }

        // 2. Fetch Student
        Student student = studentRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new IllegalArgumentException("Student with Roll No: " + rollNo + " is not registered."));

        // 3. Find Seat Assignment
        SeatAssignment assignment;
        if (request.getExamId() != null) {
            assignment = seatAssignmentRepository.findByExamIdAndStudentId(request.getExamId(), student.getId())
                    .orElseThrow(() -> new IllegalArgumentException("No seat allotted for student " + rollNo + " in this exam."));
        } else if (examCode != null) {
            final String ec = examCode;
            Exam exam = examRepository.findByExamCode(ec)
                    .orElseThrow(() -> new IllegalArgumentException("Exam " + ec + " not found."));
            assignment = seatAssignmentRepository.findByExamIdAndStudentId(exam.getId(), student.getId())
                    .orElseThrow(() -> new IllegalArgumentException("No seat allotted for student " + rollNo + " in exam " + ec));
        } else {
            assignment = seatAssignmentRepository.findByStudentRollNo(rollNo)
                    .orElseThrow(() -> new IllegalArgumentException("No current seat allotment found for student: " + rollNo));
        }

        // 4. Duplicate Check (Anti-Proxy / Anti-Duplicate Entry)
        Optional<Attendance> existing = attendanceRepository.findBySeatAssignmentId(assignment.getId());
        if (existing.isPresent()) {
            Attendance att = existing.get();
            throw new IllegalStateException("Student " + rollNo + " is ALREADY VERIFIED and marked "
                    + att.getStatus() + " at " + att.getScanTime());
        }

        // 5. Lookup Verifying Teacher
        Teacher teacher = null;
        if (request.getTeacherEmployeeId() != null) {
            teacher = teacherRepository.findByEmployeeId(request.getTeacherEmployeeId()).orElse(null);
        }

        // 6. Record Attendance
        Attendance attendance = Attendance.builder()
                .seatAssignment(assignment)
                .student(student)
                .exam(assignment.getExam())
                .verifiedByTeacher(teacher != null ? teacher : assignment.getSupervisor())
                .status(Attendance.AttendanceStatus.PRESENT)
                .scanTime(LocalDateTime.now())
                .deviceMeta(request.getDeviceMeta() != null ? request.getDeviceMeta() : "Invigilator Scanner")
                .remarks("Contactless QR verified entry")
                .build();

        attendanceRepository.save(attendance);

        // 7. Update QR Verification audit log
        qrVerificationRepository.findByStudentRollAndExamCode(student.getRollNo(), assignment.getExam().getExamCode())
                .ifPresent(qrv -> {
                    qrv.setIsUsed(true);
                    qrv.setVerifiedAt(LocalDateTime.now());
                    qrVerificationRepository.save(qrv);
                });

        log.info("Contactless QR Verified: Student {} for Exam {} at Seat {}",
                rollNo, assignment.getExam().getExamCode(), assignment.getSeatNumber());

        return mapToDTO(attendance);
    }

    public List<AttendanceDTO> getLiveAttendanceByExam(Long examId) {
        return attendanceRepository.findByExamId(examId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AttendanceDTO mapToDTO(Attendance a) {
        return AttendanceDTO.builder()
                .id(a.getId())
                .rollNo(a.getStudent().getRollNo())
                .studentName(a.getStudent().getName())
                .department(a.getStudent().getDepartment())
                .seatNumber(a.getSeatAssignment().getSeatNumber())
                .hallNumber(a.getSeatAssignment().getHall().getHallNumber())
                .examCode(a.getExam().getExamCode())
                .status(a.getStatus().name())
                .scanTime(a.getScanTime())
                .verifiedBy(a.getVerifiedByTeacher() != null ? a.getVerifiedByTeacher().getName() : "Invigilator")
                .remarks(a.getRemarks())
                .build();
    }
}
