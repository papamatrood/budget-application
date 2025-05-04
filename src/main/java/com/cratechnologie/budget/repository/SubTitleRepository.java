package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.SubTitle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubTitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubTitleRepository extends JpaRepository<SubTitle, Long>, JpaSpecificationExecutor<SubTitle> {}
