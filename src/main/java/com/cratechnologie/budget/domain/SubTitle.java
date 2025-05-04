package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubTitle.
 */
@Entity
@Table(name = "sub_title")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subTitle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subTitle", "articles" }, allowSetters = true)
    private Set<Chapter> chapters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubTitle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public SubTitle code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return this.designation;
    }

    public SubTitle designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Set<Chapter> getChapters() {
        return this.chapters;
    }

    public void setChapters(Set<Chapter> chapters) {
        if (this.chapters != null) {
            this.chapters.forEach(i -> i.setSubTitle(null));
        }
        if (chapters != null) {
            chapters.forEach(i -> i.setSubTitle(this));
        }
        this.chapters = chapters;
    }

    public SubTitle chapters(Set<Chapter> chapters) {
        this.setChapters(chapters);
        return this;
    }

    public SubTitle addChapter(Chapter chapter) {
        this.chapters.add(chapter);
        chapter.setSubTitle(this);
        return this;
    }

    public SubTitle removeChapter(Chapter chapter) {
        this.chapters.remove(chapter);
        chapter.setSubTitle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubTitle)) {
            return false;
        }
        return getId() != null && getId().equals(((SubTitle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubTitle{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", designation='" + getDesignation() + "'" +
            "}";
    }
}
