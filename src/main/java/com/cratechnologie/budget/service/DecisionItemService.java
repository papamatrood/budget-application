package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.DecisionItem;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.DecisionItem}.
 */
public interface DecisionItemService {
    /**
     * Save a decisionItem.
     *
     * @param decisionItem the entity to save.
     * @return the persisted entity.
     */
    DecisionItem save(DecisionItem decisionItem);

    /**
     * Updates a decisionItem.
     *
     * @param decisionItem the entity to update.
     * @return the persisted entity.
     */
    DecisionItem update(DecisionItem decisionItem);

    /**
     * Partially updates a decisionItem.
     *
     * @param decisionItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DecisionItem> partialUpdate(DecisionItem decisionItem);

    /**
     * Get the "id" decisionItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DecisionItem> findOne(Long id);

    /**
     * Delete the "id" decisionItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
