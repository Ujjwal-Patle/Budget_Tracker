package com.budgettracker.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        String title,
        String category,
        BigDecimal amount,
        LocalDate expenseDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
