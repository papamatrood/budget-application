package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.ArticleTestSamples.*;
import static com.cratechnologie.budget.domain.ChapterTestSamples.*;
import static com.cratechnologie.budget.domain.ExpenseTestSamples.*;
import static com.cratechnologie.budget.domain.RecipeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = getArticleSample1();
        Article article2 = new Article();
        assertThat(article1).isNotEqualTo(article2);

        article2.setId(article1.getId());
        assertThat(article1).isEqualTo(article2);

        article2 = getArticleSample2();
        assertThat(article1).isNotEqualTo(article2);
    }

    @Test
    void chapterTest() {
        Article article = getArticleRandomSampleGenerator();
        Chapter chapterBack = getChapterRandomSampleGenerator();

        article.setChapter(chapterBack);
        assertThat(article.getChapter()).isEqualTo(chapterBack);

        article.chapter(null);
        assertThat(article.getChapter()).isNull();
    }

    @Test
    void recipeTest() {
        Article article = getArticleRandomSampleGenerator();
        Recipe recipeBack = getRecipeRandomSampleGenerator();

        article.addRecipe(recipeBack);
        assertThat(article.getRecipes()).containsOnly(recipeBack);

        article.removeRecipe(recipeBack);
        assertThat(article.getRecipes()).doesNotContain(recipeBack);

        article.recipes(new HashSet<>(Set.of(recipeBack)));
        assertThat(article.getRecipes()).containsOnly(recipeBack);

        article.setRecipes(new HashSet<>());
        assertThat(article.getRecipes()).doesNotContain(recipeBack);
    }

    @Test
    void expenseTest() {
        Article article = getArticleRandomSampleGenerator();
        Expense expenseBack = getExpenseRandomSampleGenerator();

        article.addExpense(expenseBack);
        assertThat(article.getExpenses()).containsOnly(expenseBack);

        article.removeExpense(expenseBack);
        assertThat(article.getExpenses()).doesNotContain(expenseBack);

        article.expenses(new HashSet<>(Set.of(expenseBack)));
        assertThat(article.getExpenses()).containsOnly(expenseBack);

        article.setExpenses(new HashSet<>());
        assertThat(article.getExpenses()).doesNotContain(expenseBack);
    }
}
