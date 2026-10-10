package com.sea.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seat_assignments",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_exam_student", columnNames = {"exam_id", "student_id"}),
           @UniqueConstraint(name = "uk_exam_hall_seat", columnNames = {"exam_id", "hall_id", "row_number", "col_number"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supervisor_id")
    private Teacher supervisor;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "col_number", nullable = false)
    private Integer colNumber;

    @Column(name = "seat_number", nullable = false, length = 16)
    private String seatNumber; // Format: R{row}-C{col}

    @Column(name = "qr_code_data", nullable = false, length = 255)
    private String qrCodeData;

    @Lob
    @Column(name = "qr_code_image", columnDefinition = "MEDIUMTEXT")
    private String qrCodeImage; // Base64 data URL

    @Column(name = "allotted_at", updatable = false)
    private LocalDateTime allottedAt;

    @PrePersist
    protected void onCreate() {
        allottedAt = LocalDateTime.now();
    }
}
