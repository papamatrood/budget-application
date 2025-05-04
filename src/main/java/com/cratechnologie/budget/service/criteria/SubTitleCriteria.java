package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.SubTitle} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.SubTitleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sub-titles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubTitleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter designation;

    private LongFilter chapterId;

    private Boolean distinct;

    public SubTitleCriteria() {}

    public SubTitleCriteria(SubTitleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.designation = other.optionalDesignation().map(StringFilter::copy).orElse(null);
        this.chapterId = other.optionalChapterId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SubTitleCriteria copy() {
        return new SubTitleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public Optional<StringFilter> optionalDesignation() {
        return Optional.ofNullable(designation);
    }

    public StringFilter designation() {
        if (designation == null) {
            setDesignation(new StringFilter());
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public LongFilter getChapterId() {
        return chapterId;
    }

    public Optional<LongFilter> optionalChapterId() {
        return Optional.ofNullable(chapterId);
    }

    public LongFilter chapterId() {
        if (chapterId == null) {
            setChapterId(new LongFilter());
        }
        return chapterId;
    }

    public void setChapterId(LongFilter chapterId) {
        this.chapterId = chapterId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubTitleCriteria that = (SubTitleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(chapterId, that.chapterId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, designation, chapterId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubTitleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDesignation().map(f -> "designation=" + f + ", ").orElse("") +
            optionalChapterId().map(f -> "chapterId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
