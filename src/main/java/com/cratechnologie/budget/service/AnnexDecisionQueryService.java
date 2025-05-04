package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.repository.AnnexDecisionRepository;
import com.cratechnologie.budget.service.criteria.AnnexDecisionCriteria;
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
 * Service for executing complex queries for {@link AnnexDecision} entities in the database.
 * The main input is a {@link AnnexDecisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AnnexDecision} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnnexDecisionQueryService extends QueryService<AnnexDecision> {

    private static final Logger LOG = LoggerFactory.getLogger(AnnexDecisionQueryService.class);

    private final AnnexDecisionRepository annexDecisionRepository;

    public AnnexDecisionQueryService(AnnexDecisionRepository annexDecisionRepository) {
        this.annexDecisionRepository = annexDecisionRepository;
    }

    /**
     * Return a {@link Page} of {@link AnnexDecision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnnexDecision> findByCriteria(AnnexDecisionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnnexDecision> specification = createSpecification(criteria);
        return annexDecisionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnexDecisionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AnnexDecision> specification = createSpecification(criteria);
        return annexDecisionRepository.count(specification);
    }

    /**
     * Function to convert {@link AnnexDecisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnnexDecision> createSpecification(AnnexDecisionCriteria criteria) {
        Specification<AnnexDecision> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AnnexDecision_.id),
                buildStringSpecification(criteria.getDesignation(), AnnexDecision_.designation),
                buildStringSpecification(criteria.getExpenseAmount(), AnnexDecision_.expenseAmount),
                buildStringSpecification(criteria.getCreditsAlreadyOpen(), AnnexDecision_.creditsAlreadyOpen),
                buildStringSpecification(criteria.getCreditsOpen(), AnnexDecision_.creditsOpen),
                buildSpecification(criteria.getFinancialYearId(), root ->
                    root.join(AnnexDecision_.financialYear, JoinType.LEFT).get(FinancialYear_.id)
                ),
                buildSpecification(criteria.getExpenseId(), root -> root.join(AnnexDecision_.expense, JoinType.LEFT).get(Expense_.id)),
                buildSpecification(criteria.getPurchaseOrderId(), root ->
                    root.join(AnnexDecision_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                ),
                buildSpecification(criteria.getDecisionId(), root -> root.join(AnnexDecision_.decisions, JoinType.LEFT).get(Decision_.id))
            );
        }
        return specification;
    }
}
