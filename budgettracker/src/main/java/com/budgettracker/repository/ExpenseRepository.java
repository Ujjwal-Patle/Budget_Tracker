package com.budgettracker.repository;

import com.budgettracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    // Retrieve all expenses for a user within a date range
    List<Expense> findAllByUser_IdAndExpenseDateBetweenOrderByExpenseDateDesc(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate
    );

    // Retrieve expenses for a user by category and date range
    List<Expense> findAllByUser_IdAndCategoryIgnoreCaseAndExpenseDateBetweenOrderByExpenseDateDesc(
            UUID userId,
            String category,
            LocalDate startDate,
            LocalDate endDate
    );

    // Find a specific expense only if it belongs to the user
    Optional<Expense> findByIdAndUser_Id(UUID id, UUID userId);

    // Check whether an expense belongs to the user
    boolean existsByIdAndUser_Id(UUID id, UUID userId);
}
