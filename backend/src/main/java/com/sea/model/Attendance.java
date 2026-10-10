package com.sea.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seat_assignment_id", nullable = false, unique = true)
    private SeatAssignment seatAssignment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verified_by_teacher_id")
    private Teacher verifiedByTeacher;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Builder.Default
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Column(name = "scan_time")
    private LocalDateTime scanTime;

    @Column(name = "device_meta", length = 120)
    private String deviceMeta;

    @Column(length = 255)
    private String remarks;

    @PrePersist
    protected void onCreate() {
        if (scanTime == null) {
            scanTime = LocalDateTime.now();
        }
    }

    public enum AttendanceStatus {
        PRESENT,
        ABSENT,
        MALPRACTICE_FLAGGED
    }
}
