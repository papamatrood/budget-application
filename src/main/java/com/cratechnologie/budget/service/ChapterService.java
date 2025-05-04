package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.Chapter;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.Chapter}.
 */
public interface ChapterService {
    /**
     * Save a chapter.
     *
     * @param chapter the entity to save.
     * @return the persisted entity.
     */
    Chapter save(Chapter chapter);

    /**
     * Updates a chapter.
     *
     * @param chapter the entity to update.
     * @return the persisted entity.
     */
    Chapter update(Chapter chapter);

    /**
     * Partially updates a chapter.
     *
     * @param chapter the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Chapter> partialUpdate(Chapter chapter);

    /**
     * Get the "id" chapter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Chapter> findOne(Long id);

    /**
     * Delete the "id" chapter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
