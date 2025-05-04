package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.Mandate;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.Mandate}.
 */
public interface MandateService {
    /**
     * Save a mandate.
     *
     * @param mandate the entity to save.
     * @return the persisted entity.
     */
    Mandate save(Mandate mandate);

    /**
     * Updates a mandate.
     *
     * @param mandate the entity to update.
     * @return the persisted entity.
     */
    Mandate update(Mandate mandate);

    /**
     * Partially updates a mandate.
     *
     * @param mandate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mandate> partialUpdate(Mandate mandate);

    /**
     * Get the "id" mandate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mandate> findOne(Long id);

    /**
     * Delete the "id" mandate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
