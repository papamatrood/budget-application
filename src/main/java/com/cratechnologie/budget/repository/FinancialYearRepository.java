package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.FinancialYear;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FinancialYear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialYearRepository extends JpaRepository<FinancialYear, Long>, JpaSpecificationExecutor<FinancialYear> {}
