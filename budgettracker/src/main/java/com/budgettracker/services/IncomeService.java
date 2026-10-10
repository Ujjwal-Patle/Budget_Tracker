package com.budgettracker.services;

import com.budgettracker.dto.income.*;

import java.util.List;
import java.util.UUID;

public interface IncomeService {

    IncomeResponse createIncome(UUID userId, CreateIncomeRequest request);

    List<IncomeResponse> getMonthlyIncome(
            UUID userId,
            Integer year,
            Integer month
    );

    IncomeSummaryResponse getMonthlySummary(
            UUID userId,
            Integer year,
            Integer month
    );

    IncomeResponse updateIncomeStatus(
            UUID userId,
            UUID incomeId,
            UpdateIncomeStatusRequest request
    );
    IncomeResponse updateIncome(
            UUID userId,
            UUID incomeId,
            UpdateIncomeRequest request
    );

    void deleteIncome(UUID userId, UUID incomeId);

}
