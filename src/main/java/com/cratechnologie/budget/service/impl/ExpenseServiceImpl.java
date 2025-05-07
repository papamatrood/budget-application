package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.ExpenseService;
import com.cratechnologie.budget.domain.Expense;
import com.cratechnologie.budget.repository.ExpenseRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Expense}.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense save(Expense expense) {
        LOG.debug("Request to save Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    @Override
    public Expense update(Expense expense) {
        LOG.debug("Request to update Expense : {}", expense);
        return expenseRepository.save(expense);
    }

    @Override
    public Optional<Expense> partialUpdate(Expense expense) {
        LOG.debug("Request to partially update Expense : {}", expense);

        return expenseRepository
            .findById(expense.getId())
            .map(existingExpense -> {
                if (expense.getAchievementsInThePastYear() != null) {
                    existingExpense.setAchievementsInThePastYear(expense.getAchievementsInThePastYear());
                }
                if (expense.getNewYearForecast() != null) {
                    existingExpense.setNewYearForecast(expense.getNewYearForecast());
                }
                if (expense.getCategory() != null) {
                    existingExpense.setCategory(expense.getCategory());
                }

                return existingExpense;
            })
            .map(expenseRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Expense> findOne(Long id) {
        LOG.debug("Request to get Expense : {}", id);
        return expenseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Expense : {}", id);
        expenseRepository.deleteById(id);
    }
}
