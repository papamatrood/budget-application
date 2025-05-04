package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.Decision;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.Decision}.
 */
public interface DecisionService {
    /**
     * Save a decision.
     *
     * @param decision the entity to save.
     * @return the persisted entity.
     */
    Decision save(Decision decision);

    /**
     * Updates a decision.
     *
     * @param decision the entity to update.
     * @return the persisted entity.
     */
    Decision update(Decision decision);

    /**
     * Partially updates a decision.
     *
     * @param decision the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Decision> partialUpdate(Decision decision);

    /**
     * Get the "id" decision.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Decision> findOne(Long id);

    /**
     * Delete the "id" decision.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
