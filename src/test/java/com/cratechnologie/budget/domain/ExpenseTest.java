package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.AnnexDecisionTestSamples.*;
import static com.cratechnologie.budget.domain.ArticleTestSamples.*;
import static com.cratechnologie.budget.domain.ExpenseTestSamples.*;
import static com.cratechnologie.budget.domain.FinancialYearTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExpenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expense.class);
        Expense expense1 = getExpenseSample1();
        Expense expense2 = new Expense();
        assertThat(expense1).isNotEqualTo(expense2);

        expense2.setId(expense1.getId());
        assertThat(expense1).isEqualTo(expense2);

        expense2 = getExpenseSample2();
        assertThat(expense1).isNotEqualTo(expense2);
    }

    @Test
    void financialYearTest() {
        Expense expense = getExpenseRandomSampleGenerator();
        FinancialYear financialYearBack = getFinancialYearRandomSampleGenerator();

        expense.setFinancialYear(financialYearBack);
        assertThat(expense.getFinancialYear()).isEqualTo(financialYearBack);

        expense.financialYear(null);
        assertThat(expense.getFinancialYear()).isNull();
    }

    @Test
    void annexDecisionTest() {
        Expense expense = getExpenseRandomSampleGenerator();
        AnnexDecision annexDecisionBack = getAnnexDecisionRandomSampleGenerator();

        expense.setAnnexDecision(annexDecisionBack);
        assertThat(expense.getAnnexDecision()).isEqualTo(annexDecisionBack);

        expense.annexDecision(null);
        assertThat(expense.getAnnexDecision()).isNull();
    }

    @Test
    void articleTest() {
        Expense expense = getExpenseRandomSampleGenerator();
        Article articleBack = getArticleRandomSampleGenerator();

        expense.addArticle(articleBack);
        assertThat(expense.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getExpenses()).containsOnly(expense);

        expense.removeArticle(articleBack);
        assertThat(expense.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getExpenses()).doesNotContain(expense);

        expense.articles(new HashSet<>(Set.of(articleBack)));
        assertThat(expense.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getExpenses()).containsOnly(expense);

        expense.setArticles(new HashSet<>());
        assertThat(expense.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getExpenses()).doesNotContain(expense);
    }
}
