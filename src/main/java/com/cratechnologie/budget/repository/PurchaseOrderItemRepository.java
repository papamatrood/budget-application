package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.PurchaseOrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchaseOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long>, JpaSpecificationExecutor<PurchaseOrderItem> {}
