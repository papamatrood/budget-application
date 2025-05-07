package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.PurchaseOrderService;
import com.cratechnologie.budget.domain.PurchaseOrder;
import com.cratechnologie.budget.repository.PurchaseOrderRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.PurchaseOrder}.
 */
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        LOG.debug("Request to save PurchaseOrder : {}", purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    public PurchaseOrder update(PurchaseOrder purchaseOrder) {
        LOG.debug("Request to update PurchaseOrder : {}", purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    public Optional<PurchaseOrder> partialUpdate(PurchaseOrder purchaseOrder) {
        LOG.debug("Request to partially update PurchaseOrder : {}", purchaseOrder);

        return purchaseOrderRepository
            .findById(purchaseOrder.getId())
            .map(existingPurchaseOrder -> {
                if (purchaseOrder.getNameOfTheMinistry() != null) {
                    existingPurchaseOrder.setNameOfTheMinistry(purchaseOrder.getNameOfTheMinistry());
                }
                if (purchaseOrder.getOrderNumber() != null) {
                    existingPurchaseOrder.setOrderNumber(purchaseOrder.getOrderNumber());
                }
                if (purchaseOrder.getOrderDate() != null) {
                    existingPurchaseOrder.setOrderDate(purchaseOrder.getOrderDate());
                }
                if (purchaseOrder.getTotalAmountWithoutTax() != null) {
                    existingPurchaseOrder.setTotalAmountWithoutTax(purchaseOrder.getTotalAmountWithoutTax());
                }
                if (purchaseOrder.getTaxRate() != null) {
                    existingPurchaseOrder.setTaxRate(purchaseOrder.getTaxRate());
                }
                if (purchaseOrder.getTotalTaxAmount() != null) {
                    existingPurchaseOrder.setTotalTaxAmount(purchaseOrder.getTotalTaxAmount());
                }
                if (purchaseOrder.getPrepaidTaxAmount() != null) {
                    existingPurchaseOrder.setPrepaidTaxAmount(purchaseOrder.getPrepaidTaxAmount());
                }
                if (purchaseOrder.getTotalAmountWithTax() != null) {
                    existingPurchaseOrder.setTotalAmountWithTax(purchaseOrder.getTotalAmountWithTax());
                }
                if (purchaseOrder.getAuthExpenditureNumber() != null) {
                    existingPurchaseOrder.setAuthExpenditureNumber(purchaseOrder.getAuthExpenditureNumber());
                }
                if (purchaseOrder.getAllocatedCredits() != null) {
                    existingPurchaseOrder.setAllocatedCredits(purchaseOrder.getAllocatedCredits());
                }
                if (purchaseOrder.getCommittedExpenditures() != null) {
                    existingPurchaseOrder.setCommittedExpenditures(purchaseOrder.getCommittedExpenditures());
                }
                if (purchaseOrder.getAvailableBalance() != null) {
                    existingPurchaseOrder.setAvailableBalance(purchaseOrder.getAvailableBalance());
                }

                return existingPurchaseOrder;
            })
            .map(purchaseOrderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseOrder> findOne(Long id) {
        LOG.debug("Request to get PurchaseOrder : {}", id);
        return purchaseOrderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PurchaseOrder : {}", id);
        purchaseOrderRepository.deleteById(id);
    }
}
