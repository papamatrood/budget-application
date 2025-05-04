package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.domain.Article;
import com.cratechnologie.budget.repository.ArticleRepository;
import com.cratechnologie.budget.service.ArticleService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Article}.
 */
@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article save(Article article) {
        LOG.debug("Request to save Article : {}", article);
        return articleRepository.save(article);
    }

    @Override
    public Article update(Article article) {
        LOG.debug("Request to update Article : {}", article);
        return articleRepository.save(article);
    }

    @Override
    public Optional<Article> partialUpdate(Article article) {
        LOG.debug("Request to partially update Article : {}", article);

        return articleRepository
            .findById(article.getId())
            .map(existingArticle -> {
                if (article.getCategory() != null) {
                    existingArticle.setCategory(article.getCategory());
                }
                if (article.getCode() != null) {
                    existingArticle.setCode(article.getCode());
                }
                if (article.getDesignation() != null) {
                    existingArticle.setDesignation(article.getDesignation());
                }
                if (article.getAccountDiv() != null) {
                    existingArticle.setAccountDiv(article.getAccountDiv());
                }
                if (article.getCodeEnd() != null) {
                    existingArticle.setCodeEnd(article.getCodeEnd());
                }
                if (article.getParagraph() != null) {
                    existingArticle.setParagraph(article.getParagraph());
                }

                return existingArticle;
            })
            .map(articleRepository::save);
    }

    public Page<Article> findAllWithEagerRelationships(Pageable pageable) {
        return articleRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Article> findOne(Long id) {
        LOG.debug("Request to get Article : {}", id);
        return articleRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
    }
}
