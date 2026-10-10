package com.budgettracker.dto.income;

import com.budgettracker.model.IncomeStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeResponse(
        UUID id,
        String title,
        String source,
        BigDecimal amount,
        Integer incomeYear,
        Integer incomeMonth,
        IncomeStatus status,
        LocalDate expectedDate,
        LocalDate receivedDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
