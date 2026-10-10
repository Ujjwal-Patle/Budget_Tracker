package com.budgettracker.dto.income;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateIncomeRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 100)
        String title,

        @NotBlank(message = "Income source is required")
        @Size(max = 50)
        String source,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01")
        @Digits(integer = 10, fraction = 2)
        BigDecimal amount,

        @NotNull
        @Min(2000)
        @Max(2100)
        Integer incomeYear,

        @NotNull
        @Min(1)
        @Max(12)
        Integer incomeMonth,

        LocalDate expectedDate,

        @Size(max = 500)
        String notes
) {
}

