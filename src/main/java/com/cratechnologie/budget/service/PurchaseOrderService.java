package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.PurchaseOrder;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.PurchaseOrder}.
 */
public interface PurchaseOrderService {
    /**
     * Save a purchaseOrder.
     *
     * @param purchaseOrder the entity to save.
     * @return the persisted entity.
     */
    PurchaseOrder save(PurchaseOrder purchaseOrder);

    /**
     * Updates a purchaseOrder.
     *
     * @param purchaseOrder the entity to update.
     * @return the persisted entity.
     */
    PurchaseOrder update(PurchaseOrder purchaseOrder);

    /**
     * Partially updates a purchaseOrder.
     *
     * @param purchaseOrder the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchaseOrder> partialUpdate(PurchaseOrder purchaseOrder);

    /**
     * Get the "id" purchaseOrder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchaseOrder> findOne(Long id);

    /**
     * Delete the "id" purchaseOrder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
