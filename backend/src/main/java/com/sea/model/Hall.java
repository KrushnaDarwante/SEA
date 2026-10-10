package com.sea.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hall_number", nullable = false, unique = true, length = 32)
    private String hallNumber;

    @Column(nullable = false, length = 32)
    private String block;

    @Column(nullable = false, length = 16)
    @Builder.Default
    private String floor = "Ground";

    @Column(name = "total_rows", nullable = false)
    @Builder.Default
    private Integer totalRows = 5;

    @Column(name = "total_cols", nullable = false)
    @Builder.Default
    private Integer totalCols = 6;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public int getCapacity() {
        return (totalRows != null ? totalRows : 0) * (totalCols != null ? totalCols : 0);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
