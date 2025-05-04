package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.AnnexDecision;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.AnnexDecision}.
 */
public interface AnnexDecisionService {
    /**
     * Save a annexDecision.
     *
     * @param annexDecision the entity to save.
     * @return the persisted entity.
     */
    AnnexDecision save(AnnexDecision annexDecision);

    /**
     * Updates a annexDecision.
     *
     * @param annexDecision the entity to update.
     * @return the persisted entity.
     */
    AnnexDecision update(AnnexDecision annexDecision);

    /**
     * Partially updates a annexDecision.
     *
     * @param annexDecision the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnnexDecision> partialUpdate(AnnexDecision annexDecision);

    /**
     * Get all the AnnexDecision where Expense is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AnnexDecision> findAllWhereExpenseIsNull();

    /**
     * Get the "id" annexDecision.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnexDecision> findOne(Long id);

    /**
     * Delete the "id" annexDecision.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
