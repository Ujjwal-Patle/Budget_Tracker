package com.budgettracker.dto.income;

import java.math.BigDecimal;

public record IncomeSummaryResponse(
        BigDecimal expectedTotal,
        BigDecimal receivedTotal,
        BigDecimal pendingTotal
) {
}
