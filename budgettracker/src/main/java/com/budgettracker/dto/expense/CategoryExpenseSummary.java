package com.budgettracker.dto.expense;

import java.math.BigDecimal;

public record CategoryExpenseSummary(
        String category,
        BigDecimal totalSpent,
        long transactionCount
) {
}
