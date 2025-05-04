package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.AppUser;
import com.cratechnologie.budget.repository.AppUserRepository;
import com.cratechnologie.budget.service.criteria.AppUserCriteria;
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
 * Service for executing complex queries for {@link AppUser} entities in the database.
 * The main input is a {@link AppUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AppUser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppUserQueryService extends QueryService<AppUser> {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserQueryService.class);

    private final AppUserRepository appUserRepository;

    public AppUserQueryService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Return a {@link Page} of {@link AppUser} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppUser> findByCriteria(AppUserCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AppUser> specification = createSpecification(criteria);
        return appUserRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AppUser> specification = createSpecification(criteria);
        return appUserRepository.count(specification);
    }

    /**
     * Function to convert {@link AppUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AppUser> createSpecification(AppUserCriteria criteria) {
        Specification<AppUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AppUser_.id),
                buildSpecification(criteria.getAccountStatus(), AppUser_.accountStatus),
                buildRangeSpecification(criteria.getLastDateUpdate(), AppUser_.lastDateUpdate),
                buildRangeSpecification(criteria.getDateCreated(), AppUser_.dateCreated),
                buildStringSpecification(criteria.getFirstname(), AppUser_.firstname),
                buildStringSpecification(criteria.getLastname(), AppUser_.lastname),
                buildStringSpecification(criteria.getPhoneNumber(), AppUser_.phoneNumber),
                buildRangeSpecification(criteria.getBirthDate(), AppUser_.birthDate),
                buildStringSpecification(criteria.getBirthPlace(), AppUser_.birthPlace),
                buildSpecification(criteria.getGender(), AppUser_.gender),
                buildSpecification(criteria.getFamilySituation(), AppUser_.familySituation),
                buildStringSpecification(criteria.getPosition(), AppUser_.position),
                buildStringSpecification(criteria.getAddress(), AppUser_.address),
                buildSpecification(criteria.getUserId(), root -> root.join(AppUser_.user, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
