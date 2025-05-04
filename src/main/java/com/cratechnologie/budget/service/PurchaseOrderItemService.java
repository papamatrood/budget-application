package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.PurchaseOrderItem;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.PurchaseOrderItem}.
 */
public interface PurchaseOrderItemService {
    /**
     * Save a purchaseOrderItem.
     *
     * @param purchaseOrderItem the entity to save.
     * @return the persisted entity.
     */
    PurchaseOrderItem save(PurchaseOrderItem purchaseOrderItem);

    /**
     * Updates a purchaseOrderItem.
     *
     * @param purchaseOrderItem the entity to update.
     * @return the persisted entity.
     */
    PurchaseOrderItem update(PurchaseOrderItem purchaseOrderItem);

    /**
     * Partially updates a purchaseOrderItem.
     *
     * @param purchaseOrderItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchaseOrderItem> partialUpdate(PurchaseOrderItem purchaseOrderItem);

    /**
     * Get the "id" purchaseOrderItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchaseOrderItem> findOne(Long id);

    /**
     * Delete the "id" purchaseOrderItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
