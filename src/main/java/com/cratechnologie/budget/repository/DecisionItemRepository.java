package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.DecisionItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DecisionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecisionItemRepository extends JpaRepository<DecisionItem, Long>, JpaSpecificationExecutor<DecisionItem> {}
