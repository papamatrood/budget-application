package com.cratechnologie.budget.service;

import com.cratechnologie.budget.service.criteria.FinancialYearCriteria;
import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.repository.FinancialYearRepository;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FinancialYear} entities in the database.
 * The main input is a {@link FinancialYearCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FinancialYear} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinancialYearQueryService extends QueryService<FinancialYear> {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialYearQueryService.class);

    private final FinancialYearRepository financialYearRepository;

    public FinancialYearQueryService(FinancialYearRepository financialYearRepository) {
        this.financialYearRepository = financialYearRepository;
    }

    /**
     * Return a {@link Page} of {@link FinancialYear} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialYear> findByCriteria(FinancialYearCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinancialYear> specification = createSpecification(criteria);
        return financialYearRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinancialYearCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FinancialYear> specification = createSpecification(criteria);
        return financialYearRepository.count(specification);
    }

    /**
     * Function to convert {@link FinancialYearCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinancialYear> createSpecification(FinancialYearCriteria criteria) {
        Specification<FinancialYear> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FinancialYear_.id),
                buildRangeSpecification(criteria.getTheYear(), FinancialYear_.theYear),
                buildSpecification(criteria.getRecipeId(), root -> root.join(FinancialYear_.recipe, JoinType.LEFT).get(Recipe_.id)),
                buildSpecification(criteria.getExpenseId(), root -> root.join(FinancialYear_.expense, JoinType.LEFT).get(Expense_.id)),
                buildSpecification(criteria.getAnnexDecisionId(), root ->
                    root.join(FinancialYear_.annexDecision, JoinType.LEFT).get(AnnexDecision_.id)
                )
            );
        }
        return specification;
    }
}
