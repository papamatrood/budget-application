package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.AnnexDecision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AnnexDecision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnexDecisionRepository extends JpaRepository<AnnexDecision, Long>, JpaSpecificationExecutor<AnnexDecision> {}
