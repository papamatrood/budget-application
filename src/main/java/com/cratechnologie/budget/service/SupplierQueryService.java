package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.*; // for static metamodels
import com.cratechnologie.budget.domain.Supplier;
import com.cratechnologie.budget.repository.SupplierRepository;
import com.cratechnologie.budget.service.criteria.SupplierCriteria;
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
 * Service for executing complex queries for {@link Supplier} entities in the database.
 * The main input is a {@link SupplierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Supplier} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SupplierQueryService extends QueryService<Supplier> {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierQueryService.class);

    private final SupplierRepository supplierRepository;

    public SupplierQueryService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    /**
     * Return a {@link Page} of {@link Supplier} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Supplier> findByCriteria(SupplierCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SupplierCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Supplier> specification = createSpecification(criteria);
        return supplierRepository.count(specification);
    }

    /**
     * Function to convert {@link SupplierCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Supplier> createSpecification(SupplierCriteria criteria) {
        Specification<Supplier> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Supplier_.id),
                buildStringSpecification(criteria.getCompanyName(), Supplier_.companyName),
                buildStringSpecification(criteria.getAddress(), Supplier_.address),
                buildStringSpecification(criteria.getPhone(), Supplier_.phone),
                buildStringSpecification(criteria.getNifNumber(), Supplier_.nifNumber),
                buildStringSpecification(criteria.getCommercialRegister(), Supplier_.commercialRegister),
                buildStringSpecification(criteria.getBankAccount(), Supplier_.bankAccount),
                buildStringSpecification(criteria.getMandatingEstablishment(), Supplier_.mandatingEstablishment),
                buildStringSpecification(criteria.getEmail(), Supplier_.email),
                buildStringSpecification(criteria.getWebsite(), Supplier_.website),
                buildStringSpecification(criteria.getDescription(), Supplier_.description),
                buildStringSpecification(criteria.getContactFirstname(), Supplier_.contactFirstname),
                buildStringSpecification(criteria.getContactlastname(), Supplier_.contactlastname),
                buildSpecification(criteria.getPurchaseOrderId(), root ->
                    root.join(Supplier_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                )
            );
        }
        return specification;
    }
}
