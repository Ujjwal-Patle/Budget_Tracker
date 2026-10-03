package com.budgettracker.services;

import com.budgettracker.dto.BudgetResponse;
import com.budgettracker.dto.CreateBudgetRequest;

import java.util.UUID;

public interface BudgetService {

    BudgetResponse createBudget(
            UUID userId,
            CreateBudgetRequest request
    );

    BudgetResponse getBudget(
            UUID userId,
            Integer year,
            Integer month
    );
}
