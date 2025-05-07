package com.cratechnologie.budget.service;

import com.cratechnologie.budget.service.criteria.ChapterCriteria;
import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.Chapter;
import com.cratechnologie.budget.repository.ChapterRepository;

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
 * Service for executing complex queries for {@link Chapter} entities in the database.
 * The main input is a {@link ChapterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Chapter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChapterQueryService extends QueryService<Chapter> {

    private static final Logger LOG = LoggerFactory.getLogger(ChapterQueryService.class);

    private final ChapterRepository chapterRepository;

    public ChapterQueryService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    /**
     * Return a {@link Page} of {@link Chapter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Chapter> findByCriteria(ChapterCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Chapter> specification = createSpecification(criteria);
        return chapterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChapterCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Chapter> specification = createSpecification(criteria);
        return chapterRepository.count(specification);
    }

    /**
     * Function to convert {@link ChapterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Chapter> createSpecification(ChapterCriteria criteria) {
        Specification<Chapter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Chapter_.id),
                buildStringSpecification(criteria.getCode(), Chapter_.code),
                buildStringSpecification(criteria.getDesignation(), Chapter_.designation),
                buildSpecification(criteria.getSubTitleId(), root -> root.join(Chapter_.subTitle, JoinType.LEFT).get(SubTitle_.id)),
                buildSpecification(criteria.getArticleId(), root -> root.join(Chapter_.articles, JoinType.LEFT).get(Article_.id))
            );
        }
        return specification;
    }
}
