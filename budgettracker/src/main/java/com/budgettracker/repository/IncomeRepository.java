package com.budgettracker.repository;

import com.budgettracker.model.Income;
import com.budgettracker.model.IncomeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, UUID> {

    List<Income> findAllByUser_IdAndIncomeYearAndIncomeMonthOrderByExpectedDateAsc(
            UUID userId,
            Integer incomeYear,
            Integer incomeMonth
    );

    Optional<Income> findByIdAndUser_Id(UUID id, UUID userId);

    List<Income> findAllByUser_IdAndIncomeYearAndIncomeMonthAndStatus(
            UUID userId,
            Integer incomeYear,
            Integer incomeMonth,
            IncomeStatus status
    );

    boolean existsByIdAndUser_Id(UUID id, UUID userId);
}

