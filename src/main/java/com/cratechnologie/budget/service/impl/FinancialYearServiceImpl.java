package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.repository.FinancialYearRepository;
import com.cratechnologie.budget.service.FinancialYearService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.FinancialYear}.
 */
@Service
@Transactional
public class FinancialYearServiceImpl implements FinancialYearService {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialYearServiceImpl.class);

    private final FinancialYearRepository financialYearRepository;

    public FinancialYearServiceImpl(FinancialYearRepository financialYearRepository) {
        this.financialYearRepository = financialYearRepository;
    }

    @Override
    public FinancialYear save(FinancialYear financialYear) {
        LOG.debug("Request to save FinancialYear : {}", financialYear);
        return financialYearRepository.save(financialYear);
    }

    @Override
    public FinancialYear update(FinancialYear financialYear) {
        LOG.debug("Request to update FinancialYear : {}", financialYear);
        return financialYearRepository.save(financialYear);
    }

    @Override
    public Optional<FinancialYear> partialUpdate(FinancialYear financialYear) {
        LOG.debug("Request to partially update FinancialYear : {}", financialYear);

        return financialYearRepository
            .findById(financialYear.getId())
            .map(existingFinancialYear -> {
                if (financialYear.getTheYear() != null) {
                    existingFinancialYear.setTheYear(financialYear.getTheYear());
                }

                return existingFinancialYear;
            })
            .map(financialYearRepository::save);
    }

    /**
     *  Get all the financialYears where Recipe is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FinancialYear> findAllWhereRecipeIsNull() {
        LOG.debug("Request to get all financialYears where Recipe is null");
        return StreamSupport.stream(financialYearRepository.findAll().spliterator(), false)
            .filter(financialYear -> financialYear.getRecipe() == null)
            .toList();
    }

    /**
     *  Get all the financialYears where Expense is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FinancialYear> findAllWhereExpenseIsNull() {
        LOG.debug("Request to get all financialYears where Expense is null");
        return StreamSupport.stream(financialYearRepository.findAll().spliterator(), false)
            .filter(financialYear -> financialYear.getExpense() == null)
            .toList();
    }

    /**
     *  Get all the financialYears where AnnexDecision is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FinancialYear> findAllWhereAnnexDecisionIsNull() {
        LOG.debug("Request to get all financialYears where AnnexDecision is null");
        return StreamSupport.stream(financialYearRepository.findAll().spliterator(), false)
            .filter(financialYear -> financialYear.getAnnexDecision() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinancialYear> findOne(Long id) {
        LOG.debug("Request to get FinancialYear : {}", id);
        return financialYearRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete FinancialYear : {}", id);
        financialYearRepository.deleteById(id);
    }
}
