package com.budgettracker.services.servicesImpl;

import com.budgettracker.dto.expense.*;
import com.budgettracker.model.Expense;
import com.budgettracker.model.User;
import com.budgettracker.repository.ExpenseRepository;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.services.ExpenseService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            UserRepository userRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ExpenseResponse createExpense(
            UUID userId,
            CreateExpenseRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setTitle(request.title().trim());
        expense.setCategory(request.category().trim().toUpperCase());
        expense.setAmount(request.amount());
        expense.setExpenseDate(request.expenseDate());
        expense.setNotes(request.notes());

        return toResponse(expenseRepository.save(expense));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getMonthlyExpenses(
            UUID userId,
            Integer year,
            Integer month
    ) {
        YearMonth period = validateAndCreatePeriod(year, month);

        return expenseRepository
                .findAllByUser_IdAndExpenseDateBetweenOrderByExpenseDateDesc(
                        userId,
                        period.atDay(1),
                        period.atEndOfMonth()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseSummaryResponse getMonthlySummary(
            UUID userId,
            Integer year,
            Integer month
    ) {
        List<ExpenseResponse> expenses =
                getMonthlyExpenses(userId, year, month);

        BigDecimal totalSpent = expenses.stream()
                .map(ExpenseResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExpenseSummaryResponse(
                totalSpent,
                expenses.size()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryExpenseSummary> getCategorySummary(
            UUID userId,
            Integer year,
            Integer month
    ) {
        List<ExpenseResponse> expenses =
                getMonthlyExpenses(userId, year, month);

        Map<String, List<ExpenseResponse>> grouped =
                expenses.stream().collect(
                        Collectors.groupingBy(ExpenseResponse::category)
                );

        return grouped.entrySet().stream()
                .map(entry -> {
                    List<ExpenseResponse> categoryExpenses =
                            entry.getValue();

                    BigDecimal total = categoryExpenses.stream()
                            .map(ExpenseResponse::amount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new CategoryExpenseSummary(
                            entry.getKey(),
                            total,
                            categoryExpenses.size()
                    );
                })
                .sorted((a, b) ->
                        b.totalSpent().compareTo(a.totalSpent()))
                .toList();
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(
            UUID userId,
            UUID expenseId,
            UpdateExpenseRequest request
    ) {
        Expense expense = expenseRepository
                .findByIdAndUser_Id(expenseId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense record not found"
                ));

        expense.setTitle(request.title().trim());
        expense.setCategory(request.category().trim().toUpperCase());
        expense.setAmount(request.amount());
        expense.setExpenseDate(request.expenseDate());
        expense.setNotes(request.notes());

        return toResponse(expenseRepository.save(expense));
    }

    @Override
    @Transactional
    public void deleteExpense(UUID userId, UUID expenseId) {
        Expense expense = expenseRepository
                .findByIdAndUser_Id(expenseId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense record not found"
                ));

        expenseRepository.delete(expense);
    }

    private YearMonth validateAndCreatePeriod(
            Integer year,
            Integer month
    ) {
        if (year == null || year < 2000 || year > 2100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Year must be between 2000 and 2100"
            );
        }

        if (month == null || month < 1 || month > 12) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Month must be between 1 and 12"
            );
        }

        return YearMonth.of(year, month);
    }

    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getCategory(),
                expense.getAmount(),
                expense.getExpenseDate(),
                expense.getNotes(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }
}
