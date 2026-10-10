package com.budgettracker.dto.income;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateIncomeStatusRequest(

        @NotNull(message = "Status is required")
        IncomeStatusValue status,

        LocalDate receivedDate
) {
    public enum IncomeStatusValue {
        EXPECTED,
        RECEIVED
    }
}
