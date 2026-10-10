package com.sea.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AllotmentRequest {
    @NotNull(message = "Exam ID is required")
    private Long examId;

    private List<Long> hallIds; // If null/empty, all active halls are used

    private boolean enforceAntiMalpractice = true;

    private boolean randomizeWithinBranch = true;
}
