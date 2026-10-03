package com.budgettracker.repository;

import com.budgettracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    Optional<Budget> findByUserIdAndBudgetYearAndBudgetMonth(
            UUID userId,
            Integer budgetYear,
            Integer budgetMonth
    );

    boolean existsByUserIdAndBudgetYearAndBudgetMonth(
            UUID userId,
            Integer budgetYear,
            Integer budgetMonth
    );
}