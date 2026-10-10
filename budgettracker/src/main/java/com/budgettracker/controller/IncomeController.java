package com.budgettracker.controller;

import com.budgettracker.dto.income.*;
import com.budgettracker.model.User;
import com.budgettracker.services.IncomeService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> createIncome(
            @Valid @RequestBody CreateIncomeRequest request,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        IncomeResponse response =
                incomeService.createIncome(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<IncomeResponse>> getMonthlyIncome(
            @PathVariable Integer year,
            @PathVariable Integer month,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                incomeService.getMonthlyIncome(userId, year, month)
        );
    }

    @GetMapping("/{year}/{month}/summary")
    public ResponseEntity<IncomeSummaryResponse> getMonthlySummary(
            @PathVariable Integer year,
            @PathVariable Integer month,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                incomeService.getMonthlySummary(userId, year, month)
        );
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncomeResponse> updateIncomeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIncomeStatusRequest request,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        IncomeResponse response =
                incomeService.updateIncomeStatus(userId, id, request);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> updateIncome(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIncomeRequest request,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        IncomeResponse response =
                incomeService.updateIncome(userId, id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        UUID userId = getAuthenticatedUserId(authentication);

        incomeService.deleteIncome(userId, id);

        return ResponseEntity.noContent().build();
    }



}
