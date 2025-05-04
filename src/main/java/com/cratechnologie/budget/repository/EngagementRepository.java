package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.Engagement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Engagement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Long>, JpaSpecificationExecutor<Engagement> {}
