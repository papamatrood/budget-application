package com.cratechnologie.budget.repository;

import com.cratechnologie.budget.domain.Article;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ArticleRepositoryWithBagRelationships {
    Optional<Article> fetchBagRelationships(Optional<Article> article);

    List<Article> fetchBagRelationships(List<Article> articles);

    Page<Article> fetchBagRelationships(Page<Article> articles);
}
