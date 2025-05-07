package com.cratechnologie.budget.service;

import com.cratechnologie.budget.service.criteria.SubTitleCriteria;
import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.SubTitle;
import com.cratechnologie.budget.repository.SubTitleRepository;

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
 * Service for executing complex queries for {@link SubTitle} entities in the database.
 * The main input is a {@link SubTitleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SubTitle} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubTitleQueryService extends QueryService<SubTitle> {

    private static final Logger LOG = LoggerFactory.getLogger(SubTitleQueryService.class);

    private final SubTitleRepository subTitleRepository;

    public SubTitleQueryService(SubTitleRepository subTitleRepository) {
        this.subTitleRepository = subTitleRepository;
    }

    /**
     * Return a {@link Page} of {@link SubTitle} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubTitle> findByCriteria(SubTitleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubTitle> specification = createSpecification(criteria);
        return subTitleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubTitleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SubTitle> specification = createSpecification(criteria);
        return subTitleRepository.count(specification);
    }

    /**
     * Function to convert {@link SubTitleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubTitle> createSpecification(SubTitleCriteria criteria) {
        Specification<SubTitle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SubTitle_.id),
                buildStringSpecification(criteria.getCode(), SubTitle_.code),
                buildStringSpecification(criteria.getDesignation(), SubTitle_.designation),
                buildSpecification(criteria.getChapterId(), root -> root.join(SubTitle_.chapters, JoinType.LEFT).get(Chapter_.id))
            );
        }
        return specification;
    }
}
