package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.Engagement;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.Engagement}.
 */
public interface EngagementService {
    /**
     * Save a engagement.
     *
     * @param engagement the entity to save.
     * @return the persisted entity.
     */
    Engagement save(Engagement engagement);

    /**
     * Updates a engagement.
     *
     * @param engagement the entity to update.
     * @return the persisted entity.
     */
    Engagement update(Engagement engagement);

    /**
     * Partially updates a engagement.
     *
     * @param engagement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Engagement> partialUpdate(Engagement engagement);

    /**
     * Get all the Engagement where Decision is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Engagement> findAllWhereDecisionIsNull();
    /**
     * Get all the Engagement where Mandate is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Engagement> findAllWhereMandateIsNull();

    /**
     * Get the "id" engagement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Engagement> findOne(Long id);

    /**
     * Delete the "id" engagement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
