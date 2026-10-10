
package com.budgettracker.controller;

import com.budgettracker.dto.BudgetResponse;
import com.budgettracker.dto.CreateBudgetRequest;
import com.budgettracker.model.User;
import com.budgettracker.services.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    private UUID getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null ||
                !(authentication.getPrincipal() instanceof User user)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user not found"
            );
        }

        return user.getId();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse createBudget(
            @Valid @RequestBody CreateBudgetRequest request,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);
        return budgetService.createBudget(userId, request);
    }

    @GetMapping("/{year}/{month}")
    public BudgetResponse getBudget(
            @PathVariable Integer year,
            @PathVariable Integer month,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);
        return budgetService.getBudget(userId, year, month);
    }
}
