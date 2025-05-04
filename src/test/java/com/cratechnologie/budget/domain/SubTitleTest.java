package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.ChapterTestSamples.*;
import static com.cratechnologie.budget.domain.SubTitleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubTitleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubTitle.class);
        SubTitle subTitle1 = getSubTitleSample1();
        SubTitle subTitle2 = new SubTitle();
        assertThat(subTitle1).isNotEqualTo(subTitle2);

        subTitle2.setId(subTitle1.getId());
        assertThat(subTitle1).isEqualTo(subTitle2);

        subTitle2 = getSubTitleSample2();
        assertThat(subTitle1).isNotEqualTo(subTitle2);
    }

    @Test
    void chapterTest() {
        SubTitle subTitle = getSubTitleRandomSampleGenerator();
        Chapter chapterBack = getChapterRandomSampleGenerator();

        subTitle.addChapter(chapterBack);
        assertThat(subTitle.getChapters()).containsOnly(chapterBack);
        assertThat(chapterBack.getSubTitle()).isEqualTo(subTitle);

        subTitle.removeChapter(chapterBack);
        assertThat(subTitle.getChapters()).doesNotContain(chapterBack);
        assertThat(chapterBack.getSubTitle()).isNull();

        subTitle.chapters(new HashSet<>(Set.of(chapterBack)));
        assertThat(subTitle.getChapters()).containsOnly(chapterBack);
        assertThat(chapterBack.getSubTitle()).isEqualTo(subTitle);

        subTitle.setChapters(new HashSet<>());
        assertThat(subTitle.getChapters()).doesNotContain(chapterBack);
        assertThat(chapterBack.getSubTitle()).isNull();
    }
}
