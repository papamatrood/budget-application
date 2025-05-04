package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.AnnexDecisionTestSamples.*;
import static com.cratechnologie.budget.domain.ExpenseTestSamples.*;
import static com.cratechnologie.budget.domain.FinancialYearTestSamples.*;
import static com.cratechnologie.budget.domain.RecipeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancialYearTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinancialYear.class);
        FinancialYear financialYear1 = getFinancialYearSample1();
        FinancialYear financialYear2 = new FinancialYear();
        assertThat(financialYear1).isNotEqualTo(financialYear2);

        financialYear2.setId(financialYear1.getId());
        assertThat(financialYear1).isEqualTo(financialYear2);

        financialYear2 = getFinancialYearSample2();
        assertThat(financialYear1).isNotEqualTo(financialYear2);
    }

    @Test
    void recipeTest() {
        FinancialYear financialYear = getFinancialYearRandomSampleGenerator();
        Recipe recipeBack = getRecipeRandomSampleGenerator();

        financialYear.setRecipe(recipeBack);
        assertThat(financialYear.getRecipe()).isEqualTo(recipeBack);
        assertThat(recipeBack.getFinancialYear()).isEqualTo(financialYear);

        financialYear.recipe(null);
        assertThat(financialYear.getRecipe()).isNull();
        assertThat(recipeBack.getFinancialYear()).isNull();
    }

    @Test
    void expenseTest() {
        FinancialYear financialYear = getFinancialYearRandomSampleGenerator();
        Expense expenseBack = getExpenseRandomSampleGenerator();

        financialYear.setExpense(expenseBack);
        assertThat(financialYear.getExpense()).isEqualTo(expenseBack);
        assertThat(expenseBack.getFinancialYear()).isEqualTo(financialYear);

        financialYear.expense(null);
        assertThat(financialYear.getExpense()).isNull();
        assertThat(expenseBack.getFinancialYear()).isNull();
    }

    @Test
    void annexDecisionTest() {
        FinancialYear financialYear = getFinancialYearRandomSampleGenerator();
        AnnexDecision annexDecisionBack = getAnnexDecisionRandomSampleGenerator();

        financialYear.setAnnexDecision(annexDecisionBack);
        assertThat(financialYear.getAnnexDecision()).isEqualTo(annexDecisionBack);
        assertThat(annexDecisionBack.getFinancialYear()).isEqualTo(financialYear);

        financialYear.annexDecision(null);
        assertThat(financialYear.getAnnexDecision()).isNull();
        assertThat(annexDecisionBack.getFinancialYear()).isNull();
    }
}
