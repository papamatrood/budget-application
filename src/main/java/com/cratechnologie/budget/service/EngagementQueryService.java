package com.cratechnologie.budget.service;

import com.cratechnologie.budget.service.criteria.EngagementCriteria;
import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.repository.EngagementRepository;

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
 * Service for executing complex queries for {@link Engagement} entities in the database.
 * The main input is a {@link EngagementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Engagement} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EngagementQueryService extends QueryService<Engagement> {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementQueryService.class);

    private final EngagementRepository engagementRepository;

    public EngagementQueryService(EngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
    }

    /**
     * Return a {@link Page} of {@link Engagement} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Engagement> findByCriteria(EngagementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Engagement> specification = createSpecification(criteria);
        return engagementRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EngagementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Engagement> specification = createSpecification(criteria);
        return engagementRepository.count(specification);
    }

    /**
     * Function to convert {@link EngagementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Engagement> createSpecification(EngagementCriteria criteria) {
        Specification<Engagement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Engagement_.id),
                buildStringSpecification(criteria.getEngagementNumber(), Engagement_.engagementNumber),
                buildRangeSpecification(criteria.getEngagementDate(), Engagement_.engagementDate),
                buildStringSpecification(criteria.getObjectOfExpense(), Engagement_.objectOfExpense),
                buildStringSpecification(criteria.getNotifiedCredits(), Engagement_.notifiedCredits),
                buildStringSpecification(criteria.getCreditCommitted(), Engagement_.creditCommitted),
                buildStringSpecification(criteria.getCreditsAvailable(), Engagement_.creditsAvailable),
                buildStringSpecification(criteria.getAmountProposedCommitment(), Engagement_.amountProposedCommitment),
                buildStringSpecification(criteria.getHeadDaf(), Engagement_.headDaf),
                buildStringSpecification(criteria.getFinancialController(), Engagement_.financialController),
                buildStringSpecification(criteria.getGeneralManager(), Engagement_.generalManager),
                buildSpecification(criteria.getDecisionId(), root -> root.join(Engagement_.decision, JoinType.LEFT).get(Decision_.id)),
                buildSpecification(criteria.getMandateId(), root -> root.join(Engagement_.mandate, JoinType.LEFT).get(Mandate_.id)),
                buildSpecification(criteria.getPurchaseOrderId(), root ->
                    root.join(Engagement_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                )
            );
        }
        return specification;
    }
}
