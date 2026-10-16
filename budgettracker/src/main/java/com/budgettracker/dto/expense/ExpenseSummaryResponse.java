package com.budgettracker.dto.expense;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(
        BigDecimal totalSpent,
        long transactionCount
) {
}
