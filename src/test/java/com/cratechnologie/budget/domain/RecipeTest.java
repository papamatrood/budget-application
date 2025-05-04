package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.ArticleTestSamples.*;
import static com.cratechnologie.budget.domain.FinancialYearTestSamples.*;
import static com.cratechnologie.budget.domain.RecipeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecipeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipe.class);
        Recipe recipe1 = getRecipeSample1();
        Recipe recipe2 = new Recipe();
        assertThat(recipe1).isNotEqualTo(recipe2);

        recipe2.setId(recipe1.getId());
        assertThat(recipe1).isEqualTo(recipe2);

        recipe2 = getRecipeSample2();
        assertThat(recipe1).isNotEqualTo(recipe2);
    }

    @Test
    void financialYearTest() {
        Recipe recipe = getRecipeRandomSampleGenerator();
        FinancialYear financialYearBack = getFinancialYearRandomSampleGenerator();

        recipe.setFinancialYear(financialYearBack);
        assertThat(recipe.getFinancialYear()).isEqualTo(financialYearBack);

        recipe.financialYear(null);
        assertThat(recipe.getFinancialYear()).isNull();
    }

    @Test
    void articleTest() {
        Recipe recipe = getRecipeRandomSampleGenerator();
        Article articleBack = getArticleRandomSampleGenerator();

        recipe.addArticle(articleBack);
        assertThat(recipe.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getRecipes()).containsOnly(recipe);

        recipe.removeArticle(articleBack);
        assertThat(recipe.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getRecipes()).doesNotContain(recipe);

        recipe.articles(new HashSet<>(Set.of(articleBack)));
        assertThat(recipe.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getRecipes()).containsOnly(recipe);

        recipe.setArticles(new HashSet<>());
        assertThat(recipe.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getRecipes()).doesNotContain(recipe);
    }
}
