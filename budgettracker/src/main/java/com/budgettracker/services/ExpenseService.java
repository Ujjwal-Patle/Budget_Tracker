package com.budgettracker.services;

import com.budgettracker.dto.expense.CategoryExpenseSummary;
import com.budgettracker.dto.expense.CreateExpenseRequest;
import com.budgettracker.dto.expense.ExpenseResponse;
import com.budgettracker.dto.expense.ExpenseSummaryResponse;
import com.budgettracker.dto.expense.UpdateExpenseRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseResponse createExpense(
            UUID userId,
            CreateExpenseRequest request
    );

    List<ExpenseResponse> getMonthlyExpenses(
            UUID userId,
            Integer year,
            Integer month
    );

    ExpenseSummaryResponse getMonthlySummary(
            UUID userId,
            Integer year,
            Integer month
    );

    List<CategoryExpenseSummary> getCategorySummary(
            UUID userId,
            Integer year,
            Integer month
    );

    ExpenseResponse updateExpense(
            UUID userId,
            UUID expenseId,
            UpdateExpenseRequest request
    );

    void deleteExpense(UUID userId, UUID expenseId);
}
