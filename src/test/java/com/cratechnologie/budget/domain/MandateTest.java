package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.EngagementTestSamples.*;
import static com.cratechnologie.budget.domain.MandateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MandateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mandate.class);
        Mandate mandate1 = getMandateSample1();
        Mandate mandate2 = new Mandate();
        assertThat(mandate1).isNotEqualTo(mandate2);

        mandate2.setId(mandate1.getId());
        assertThat(mandate1).isEqualTo(mandate2);

        mandate2 = getMandateSample2();
        assertThat(mandate1).isNotEqualTo(mandate2);
    }

    @Test
    void engagementTest() {
        Mandate mandate = getMandateRandomSampleGenerator();
        Engagement engagementBack = getEngagementRandomSampleGenerator();

        mandate.setEngagement(engagementBack);
        assertThat(mandate.getEngagement()).isEqualTo(engagementBack);

        mandate.engagement(null);
        assertThat(mandate.getEngagement()).isNull();
    }
}
