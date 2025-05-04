package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.Decision;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Decision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long>, JpaSpecificationExecutor<Decision> {}
