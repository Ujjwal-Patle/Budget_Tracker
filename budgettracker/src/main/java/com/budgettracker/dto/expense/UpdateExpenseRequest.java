package com.budgettracker.dto.expense;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 100)
        String title,

        @NotBlank(message = "Category is required")
        @Size(max = 50)
        String category,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 10, fraction = 2)
        BigDecimal amount,

        @NotNull(message = "Expense date is required")
        @PastOrPresent(message = "Expense date cannot be in the future")
        LocalDate expenseDate,

        @Size(max = 500)
        String notes
) {
}
