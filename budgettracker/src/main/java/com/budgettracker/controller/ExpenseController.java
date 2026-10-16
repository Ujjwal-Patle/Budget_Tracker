
package com.budgettracker.controller;

import com.budgettracker.dto.expense.CreateExpenseRequest;
import com.budgettracker.dto.expense.UpdateExpenseRequest;
import com.budgettracker.dto.expense.ExpenseResponse;
import com.budgettracker.dto.expense.ExpenseSummaryResponse;
import com.budgettracker.dto.expense.CategoryExpenseSummary;
import com.budgettracker.model.User;
import com.budgettracker.services.ExpenseService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody CreateExpenseRequest request,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        ExpenseResponse response =
                expenseService.createExpense(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<ExpenseResponse>> getMonthlyExpenses(
            @PathVariable Integer year,
            @PathVariable Integer month,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expenseService.getMonthlyExpenses(userId, year, month)
        );
    }

    @GetMapping("/{year}/{month}/summary")
    public ResponseEntity<ExpenseSummaryResponse> getMonthlySummary(
            @PathVariable Integer year,
            @PathVariable Integer month,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expenseService.getMonthlySummary(userId, year, month)
        );
    }

    @GetMapping("/{year}/{month}/categories")
    public ResponseEntity<List<CategoryExpenseSummary>> getCategorySummary(
            @PathVariable Integer year,
            @PathVariable Integer month,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expenseService.getCategorySummary(userId, year, month)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExpenseRequest request,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                expenseService.updateExpense(userId, id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        expenseService.deleteExpense(userId, id);

        return ResponseEntity.noContent().build();
    }

    private UUID getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null
                || !(authentication.getPrincipal() instanceof User user)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user not found"
            );
        }

        return user.getId();
    }
}
