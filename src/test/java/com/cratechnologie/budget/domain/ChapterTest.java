package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.ArticleTestSamples.*;
import static com.cratechnologie.budget.domain.ChapterTestSamples.*;
import static com.cratechnologie.budget.domain.SubTitleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChapterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chapter.class);
        Chapter chapter1 = getChapterSample1();
        Chapter chapter2 = new Chapter();
        assertThat(chapter1).isNotEqualTo(chapter2);

        chapter2.setId(chapter1.getId());
        assertThat(chapter1).isEqualTo(chapter2);

        chapter2 = getChapterSample2();
        assertThat(chapter1).isNotEqualTo(chapter2);
    }

    @Test
    void subTitleTest() {
        Chapter chapter = getChapterRandomSampleGenerator();
        SubTitle subTitleBack = getSubTitleRandomSampleGenerator();

        chapter.setSubTitle(subTitleBack);
        assertThat(chapter.getSubTitle()).isEqualTo(subTitleBack);

        chapter.subTitle(null);
        assertThat(chapter.getSubTitle()).isNull();
    }

    @Test
    void articleTest() {
        Chapter chapter = getChapterRandomSampleGenerator();
        Article articleBack = getArticleRandomSampleGenerator();

        chapter.addArticle(articleBack);
        assertThat(chapter.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getChapter()).isEqualTo(chapter);

        chapter.removeArticle(articleBack);
        assertThat(chapter.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getChapter()).isNull();

        chapter.articles(new HashSet<>(Set.of(articleBack)));
        assertThat(chapter.getArticles()).containsOnly(articleBack);
        assertThat(articleBack.getChapter()).isEqualTo(chapter);

        chapter.setArticles(new HashSet<>());
        assertThat(chapter.getArticles()).doesNotContain(articleBack);
        assertThat(articleBack.getChapter()).isNull();
    }
}
