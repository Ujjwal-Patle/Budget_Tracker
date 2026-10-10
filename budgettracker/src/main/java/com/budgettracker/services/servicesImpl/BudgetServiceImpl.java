package com.budgettracker.services.servicesImpl;

import com.budgettracker.dto.BudgetResponse;
import com.budgettracker.dto.CreateBudgetRequest;
import com.budgettracker.model.Budget;
import com.budgettracker.model.User;
import com.budgettracker.repository.BudgetRepository;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.services.BudgetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetServiceImpl(
            BudgetRepository budgetRepository,
            UserRepository userRepository
    ) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BudgetResponse createBudget(
            UUID userId,
            CreateBudgetRequest request
    ) {
        if (budgetRepository.existsByUserIdAndBudgetYearAndBudgetMonth(
                userId, request.getBudgetYear(), request.getBudgetMonth()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A budget already exists for this month"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setBudgetYear(request.getBudgetYear());
        budget.setBudgetMonth(request.getBudgetMonth());
        budget.setCurrency(user.getCurrency());

        Budget saved = budgetRepository.save(budget);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetResponse getBudget(
            UUID userId,
            Integer year,
            Integer month
    ) {
        Budget budget = budgetRepository
                .findByUserIdAndBudgetYearAndBudgetMonth(userId, year, month)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Budget not found"
                ));

        return toResponse(budget);
    }

    private BudgetResponse toResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getBudgetYear(),
                budget.getBudgetMonth(),
                budget.getCurrency(),
                budget.getCreatedAt(),
                budget.getUpdatedAt()
        );
    }
}