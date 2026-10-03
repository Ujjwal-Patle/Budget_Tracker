package com.budgettracker.dto;

import java.time.Instant;
import java.util.UUID;

public class BudgetResponse {

    private UUID id;
    private Integer budgetYear;
    private Integer budgetMonth;
    private String currency;
    private Instant createdAt;
    private Instant updatedAt;

    public BudgetResponse(
            UUID id,
            Integer budgetYear,
            Integer budgetMonth,
            String currency,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.budgetYear = budgetYear;
        this.budgetMonth = budgetMonth;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public Integer getBudgetYear() { return budgetYear; }
    public Integer getBudgetMonth() { return budgetMonth; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}