package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.AnnexDecisionTestSamples.*;
import static com.cratechnologie.budget.domain.DecisionItemTestSamples.*;
import static com.cratechnologie.budget.domain.DecisionTestSamples.*;
import static com.cratechnologie.budget.domain.EngagementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DecisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Decision.class);
        Decision decision1 = getDecisionSample1();
        Decision decision2 = new Decision();
        assertThat(decision1).isNotEqualTo(decision2);

        decision2.setId(decision1.getId());
        assertThat(decision1).isEqualTo(decision2);

        decision2 = getDecisionSample2();
        assertThat(decision1).isNotEqualTo(decision2);
    }

    @Test
    void engagementTest() {
        Decision decision = getDecisionRandomSampleGenerator();
        Engagement engagementBack = getEngagementRandomSampleGenerator();

        decision.setEngagement(engagementBack);
        assertThat(decision.getEngagement()).isEqualTo(engagementBack);

        decision.engagement(null);
        assertThat(decision.getEngagement()).isNull();
    }

    @Test
    void annexDecisionTest() {
        Decision decision = getDecisionRandomSampleGenerator();
        AnnexDecision annexDecisionBack = getAnnexDecisionRandomSampleGenerator();

        decision.setAnnexDecision(annexDecisionBack);
        assertThat(decision.getAnnexDecision()).isEqualTo(annexDecisionBack);

        decision.annexDecision(null);
        assertThat(decision.getAnnexDecision()).isNull();
    }

    @Test
    void decisionItemTest() {
        Decision decision = getDecisionRandomSampleGenerator();
        DecisionItem decisionItemBack = getDecisionItemRandomSampleGenerator();

        decision.addDecisionItem(decisionItemBack);
        assertThat(decision.getDecisionItems()).containsOnly(decisionItemBack);
        assertThat(decisionItemBack.getDecision()).isEqualTo(decision);

        decision.removeDecisionItem(decisionItemBack);
        assertThat(decision.getDecisionItems()).doesNotContain(decisionItemBack);
        assertThat(decisionItemBack.getDecision()).isNull();

        decision.decisionItems(new HashSet<>(Set.of(decisionItemBack)));
        assertThat(decision.getDecisionItems()).containsOnly(decisionItemBack);
        assertThat(decisionItemBack.getDecision()).isEqualTo(decision);

        decision.setDecisionItems(new HashSet<>());
        assertThat(decision.getDecisionItems()).doesNotContain(decisionItemBack);
        assertThat(decisionItemBack.getDecision()).isNull();
    }
}
