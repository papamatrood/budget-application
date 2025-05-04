package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.domain.PurchaseOrderItem;
import com.cratechnologie.budget.repository.PurchaseOrderItemRepository;
import com.cratechnologie.budget.service.PurchaseOrderItemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.PurchaseOrderItem}.
 */
@Service
@Transactional
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderItemServiceImpl.class);

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    public PurchaseOrderItemServiceImpl(PurchaseOrderItemRepository purchaseOrderItemRepository) {
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
    }

    @Override
    public PurchaseOrderItem save(PurchaseOrderItem purchaseOrderItem) {
        LOG.debug("Request to save PurchaseOrderItem : {}", purchaseOrderItem);
        return purchaseOrderItemRepository.save(purchaseOrderItem);
    }

    @Override
    public PurchaseOrderItem update(PurchaseOrderItem purchaseOrderItem) {
        LOG.debug("Request to update PurchaseOrderItem : {}", purchaseOrderItem);
        return purchaseOrderItemRepository.save(purchaseOrderItem);
    }

    @Override
    public Optional<PurchaseOrderItem> partialUpdate(PurchaseOrderItem purchaseOrderItem) {
        LOG.debug("Request to partially update PurchaseOrderItem : {}", purchaseOrderItem);

        return purchaseOrderItemRepository
            .findById(purchaseOrderItem.getId())
            .map(existingPurchaseOrderItem -> {
                if (purchaseOrderItem.getProductName() != null) {
                    existingPurchaseOrderItem.setProductName(purchaseOrderItem.getProductName());
                }
                if (purchaseOrderItem.getQuantity() != null) {
                    existingPurchaseOrderItem.setQuantity(purchaseOrderItem.getQuantity());
                }
                if (purchaseOrderItem.getUnitPrice() != null) {
                    existingPurchaseOrderItem.setUnitPrice(purchaseOrderItem.getUnitPrice());
                }
                if (purchaseOrderItem.getTotalAmount() != null) {
                    existingPurchaseOrderItem.setTotalAmount(purchaseOrderItem.getTotalAmount());
                }

                return existingPurchaseOrderItem;
            })
            .map(purchaseOrderItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrderItem> findOne(Long id) {
        LOG.debug("Request to get PurchaseOrderItem : {}", id);
        return purchaseOrderItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PurchaseOrderItem : {}", id);
        purchaseOrderItemRepository.deleteById(id);
    }
}
