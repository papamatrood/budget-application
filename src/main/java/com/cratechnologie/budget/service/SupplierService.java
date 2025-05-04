package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.Supplier;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.Supplier}.
 */
public interface SupplierService {
    /**
     * Save a supplier.
     *
     * @param supplier the entity to save.
     * @return the persisted entity.
     */
    Supplier save(Supplier supplier);

    /**
     * Updates a supplier.
     *
     * @param supplier the entity to update.
     * @return the persisted entity.
     */
    Supplier update(Supplier supplier);

    /**
     * Partially updates a supplier.
     *
     * @param supplier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Supplier> partialUpdate(Supplier supplier);

    /**
     * Get the "id" supplier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Supplier> findOne(Long id);

    /**
     * Delete the "id" supplier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
