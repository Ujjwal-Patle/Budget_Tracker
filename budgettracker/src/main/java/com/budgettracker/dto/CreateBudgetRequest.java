package com.budgettracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateBudgetRequest {

    @NotNull
    @Min(2000)
    @Max(2100)
    private Integer budgetYear;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer budgetMonth;

    public Integer getBudgetYear() {
        return budgetYear;
    }

    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }

    public Integer getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(Integer budgetMonth) {
        this.budgetMonth = budgetMonth;
    }
}
