package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.AnnexDecisionTestSamples.*;
import static com.cratechnologie.budget.domain.DecisionTestSamples.*;
import static com.cratechnologie.budget.domain.ExpenseTestSamples.*;
import static com.cratechnologie.budget.domain.FinancialYearTestSamples.*;
import static com.cratechnologie.budget.domain.PurchaseOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AnnexDecisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnexDecision.class);
        AnnexDecision annexDecision1 = getAnnexDecisionSample1();
        AnnexDecision annexDecision2 = new AnnexDecision();
        assertThat(annexDecision1).isNotEqualTo(annexDecision2);

        annexDecision2.setId(annexDecision1.getId());
        assertThat(annexDecision1).isEqualTo(annexDecision2);

        annexDecision2 = getAnnexDecisionSample2();
        assertThat(annexDecision1).isNotEqualTo(annexDecision2);
    }

    @Test
    void financialYearTest() {
        AnnexDecision annexDecision = getAnnexDecisionRandomSampleGenerator();
        FinancialYear financialYearBack = getFinancialYearRandomSampleGenerator();

        annexDecision.setFinancialYear(financialYearBack);
        assertThat(annexDecision.getFinancialYear()).isEqualTo(financialYearBack);

        annexDecision.financialYear(null);
        assertThat(annexDecision.getFinancialYear()).isNull();
    }

    @Test
    void expenseTest() {
        AnnexDecision annexDecision = getAnnexDecisionRandomSampleGenerator();
        Expense expenseBack = getExpenseRandomSampleGenerator();

        annexDecision.setExpense(expenseBack);
        assertThat(annexDecision.getExpense()).isEqualTo(expenseBack);
        assertThat(expenseBack.getAnnexDecision()).isEqualTo(annexDecision);

        annexDecision.expense(null);
        assertThat(annexDecision.getExpense()).isNull();
        assertThat(expenseBack.getAnnexDecision()).isNull();
    }

    @Test
    void purchaseOrderTest() {
        AnnexDecision annexDecision = getAnnexDecisionRandomSampleGenerator();
        PurchaseOrder purchaseOrderBack = getPurchaseOrderRandomSampleGenerator();

        annexDecision.addPurchaseOrder(purchaseOrderBack);
        assertThat(annexDecision.getPurchaseOrders()).containsOnly(purchaseOrderBack);
        assertThat(purchaseOrderBack.getAnnexDecision()).isEqualTo(annexDecision);

        annexDecision.removePurchaseOrder(purchaseOrderBack);
        assertThat(annexDecision.getPurchaseOrders()).doesNotContain(purchaseOrderBack);
        assertThat(purchaseOrderBack.getAnnexDecision()).isNull();

        annexDecision.purchaseOrders(new HashSet<>(Set.of(purchaseOrderBack)));
        assertThat(annexDecision.getPurchaseOrders()).containsOnly(purchaseOrderBack);
        assertThat(purchaseOrderBack.getAnnexDecision()).isEqualTo(annexDecision);

        annexDecision.setPurchaseOrders(new HashSet<>());
        assertThat(annexDecision.getPurchaseOrders()).doesNotContain(purchaseOrderBack);
        assertThat(purchaseOrderBack.getAnnexDecision()).isNull();
    }

    @Test
    void decisionTest() {
        AnnexDecision annexDecision = getAnnexDecisionRandomSampleGenerator();
        Decision decisionBack = getDecisionRandomSampleGenerator();

        annexDecision.addDecision(decisionBack);
        assertThat(annexDecision.getDecisions()).containsOnly(decisionBack);
        assertThat(decisionBack.getAnnexDecision()).isEqualTo(annexDecision);

        annexDecision.removeDecision(decisionBack);
        assertThat(annexDecision.getDecisions()).doesNotContain(decisionBack);
        assertThat(decisionBack.getAnnexDecision()).isNull();

        annexDecision.decisions(new HashSet<>(Set.of(decisionBack)));
        assertThat(annexDecision.getDecisions()).containsOnly(decisionBack);
        assertThat(decisionBack.getAnnexDecision()).isEqualTo(annexDecision);

        annexDecision.setDecisions(new HashSet<>());
        assertThat(annexDecision.getDecisions()).doesNotContain(decisionBack);
        assertThat(decisionBack.getAnnexDecision()).isNull();
    }
}
