package com.budgettracker.services.servicesImpl;

import com.budgettracker.dto.income.*;
import com.budgettracker.model.Income;
import com.budgettracker.model.IncomeStatus;
import com.budgettracker.model.User;
import com.budgettracker.repository.IncomeRepository;
import com.budgettracker.repository.UserRepository;
import com.budgettracker.services.IncomeService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeServiceImpl(
            IncomeRepository incomeRepository,
            UserRepository userRepository
    ) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public IncomeResponse createIncome(
            UUID userId,
            CreateIncomeRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Income income = new Income();
        income.setUser(user);
        income.setTitle(request.title().trim());
        income.setSource(request.source().trim());
        income.setAmount(request.amount());
        income.setIncomeYear(request.incomeYear());
        income.setIncomeMonth(request.incomeMonth());
        income.setStatus(IncomeStatus.EXPECTED);
        income.setExpectedDate(request.expectedDate());
        income.setReceivedDate(null);
        income.setNotes(request.notes());

        Income savedIncome = incomeRepository.save(income);

        return toResponse(savedIncome);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeResponse> getMonthlyIncome(
            UUID userId,
            Integer year,
            Integer month
    ) {
        validateYearAndMonth(year, month);

        return incomeRepository
                .findAllByUser_IdAndIncomeYearAndIncomeMonthOrderByExpectedDateAsc(
                        userId,
                        year,
                        month
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeSummaryResponse getMonthlySummary(
            UUID userId,
            Integer year,
            Integer month
    ) {
        validateYearAndMonth(year, month);

        List<Income> expectedIncome =
                incomeRepository
                        .findAllByUser_IdAndIncomeYearAndIncomeMonthAndStatus(
                                userId, year, month, IncomeStatus.EXPECTED
                        );

        List<Income> receivedIncome =
                incomeRepository
                        .findAllByUser_IdAndIncomeYearAndIncomeMonthAndStatus(
                                userId, year, month, IncomeStatus.RECEIVED
                        );

        BigDecimal expectedTotal = expectedIncome.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal receivedTotal = receivedIncome.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new IncomeSummaryResponse(
                expectedTotal.add(receivedTotal),
                receivedTotal,
                expectedTotal
        );
    }

    private IncomeResponse toResponse(Income income) {
        return new IncomeResponse(
                income.getId(),
                income.getTitle(),
                income.getSource(),
                income.getAmount(),
                income.getIncomeYear(),
                income.getIncomeMonth(),
                income.getStatus(),
                income.getExpectedDate(),
                income.getReceivedDate(),
                income.getNotes(),
                income.getCreatedAt(),
                income.getUpdatedAt()
        );
    }

    private void validateYearAndMonth(Integer year, Integer month) {
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
    }

    @Override
    @Transactional
    public IncomeResponse updateIncomeStatus(
            UUID userId,
            UUID incomeId,
            UpdateIncomeStatusRequest request
    ) {
        Income income = incomeRepository.findByIdAndUser_Id(incomeId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Income record not found"
                ));

        UpdateIncomeStatusRequest.IncomeStatusValue requestedStatus =
                request.status();

        if (requestedStatus == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required"
            );
        }

        if (requestedStatus == UpdateIncomeStatusRequest.IncomeStatusValue.RECEIVED) {
            if (request.receivedDate() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Received date is required when marking income as received"
                );
            }

            income.setStatus(IncomeStatus.RECEIVED);
            income.setReceivedDate(request.receivedDate());
        } else {
            if (request.receivedDate() != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Received date must be null when status is EXPECTED"
                );
            }

            income.setStatus(IncomeStatus.EXPECTED);
            income.setReceivedDate(null);
        }

        Income updatedIncome = incomeRepository.save(income);

        return toResponse(updatedIncome);
    }


    @Override
    @Transactional
    public IncomeResponse updateIncome(
            UUID userId,
            UUID incomeId,
            UpdateIncomeRequest request
    ) {
        Income income = incomeRepository.findByIdAndUser_Id(incomeId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Income record not found"
                ));

        income.setTitle(request.title().trim());
        income.setSource(request.source().trim());
        income.setAmount(request.amount());
        income.setIncomeYear(request.incomeYear());
        income.setIncomeMonth(request.incomeMonth());
        income.setExpectedDate(request.expectedDate());
        income.setNotes(request.notes());

        // Preserve the existing status and received date.
        Income updatedIncome = incomeRepository.save(income);

        return toResponse(updatedIncome);
    }


    @Override
    @Transactional
    public void deleteIncome(UUID userId, UUID incomeId) {
        Income income = incomeRepository
                .findByIdAndUser_Id(incomeId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Income record not found"
                ));

        incomeRepository.delete(income);
    }


}
