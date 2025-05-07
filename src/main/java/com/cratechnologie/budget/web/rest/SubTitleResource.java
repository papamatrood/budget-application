package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.SubTitleQueryService;
import com.cratechnologie.budget.service.SubTitleService;
import com.cratechnologie.budget.service.criteria.SubTitleCriteria;
import com.cratechnologie.budget.domain.SubTitle;
import com.cratechnologie.budget.repository.SubTitleRepository;
import com.cratechnologie.budget.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cratechnologie.budget.domain.SubTitle}.
 */
@RestController
@RequestMapping("/api/sub-titles")
public class SubTitleResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubTitleResource.class);

    private static final String ENTITY_NAME = "subTitle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubTitleService subTitleService;

    private final SubTitleRepository subTitleRepository;

    private final SubTitleQueryService subTitleQueryService;

    public SubTitleResource(
        SubTitleService subTitleService,
        SubTitleRepository subTitleRepository,
        SubTitleQueryService subTitleQueryService
    ) {
        this.subTitleService = subTitleService;
        this.subTitleRepository = subTitleRepository;
        this.subTitleQueryService = subTitleQueryService;
    }

    /**
     * {@code POST  /sub-titles} : Create a new subTitle.
     *
     * @param subTitle the subTitle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subTitle, or with status {@code 400 (Bad Request)} if the subTitle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubTitle> createSubTitle(@Valid @RequestBody SubTitle subTitle) throws URISyntaxException {
        LOG.debug("REST request to save SubTitle : {}", subTitle);
        if (subTitle.getId() != null) {
            throw new BadRequestAlertException("A new subTitle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subTitle = subTitleService.save(subTitle);
        return ResponseEntity.created(new URI("/api/sub-titles/" + subTitle.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, subTitle.getId().toString()))
            .body(subTitle);
    }

    /**
     * {@code PUT  /sub-titles/:id} : Updates an existing subTitle.
     *
     * @param id the id of the subTitle to save.
     * @param subTitle the subTitle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subTitle,
     * or with status {@code 400 (Bad Request)} if the subTitle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subTitle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubTitle> updateSubTitle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubTitle subTitle
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubTitle : {}, {}", id, subTitle);
        if (subTitle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subTitle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subTitleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subTitle = subTitleService.update(subTitle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subTitle.getId().toString()))
            .body(subTitle);
    }

    /**
     * {@code PATCH  /sub-titles/:id} : Partial updates given fields of an existing subTitle, field will ignore if it is null
     *
     * @param id the id of the subTitle to save.
     * @param subTitle the subTitle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subTitle,
     * or with status {@code 400 (Bad Request)} if the subTitle is not valid,
     * or with status {@code 404 (Not Found)} if the subTitle is not found,
     * or with status {@code 500 (Internal Server Error)} if the subTitle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubTitle> partialUpdateSubTitle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubTitle subTitle
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubTitle partially : {}, {}", id, subTitle);
        if (subTitle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subTitle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subTitleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubTitle> result = subTitleService.partialUpdate(subTitle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subTitle.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-titles} : get all the subTitles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subTitles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubTitle>> getAllSubTitles(
        SubTitleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SubTitles by criteria: {}", criteria);

        Page<SubTitle> page = subTitleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-titles/count} : count all the subTitles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSubTitles(SubTitleCriteria criteria) {
        LOG.debug("REST request to count SubTitles by criteria: {}", criteria);
        return ResponseEntity.ok().body(subTitleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-titles/:id} : get the "id" subTitle.
     *
     * @param id the id of the subTitle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subTitle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubTitle> getSubTitle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubTitle : {}", id);
        Optional<SubTitle> subTitle = subTitleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subTitle);
    }

    /**
     * {@code DELETE  /sub-titles/:id} : delete the "id" subTitle.
     *
     * @param id the id of the subTitle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTitle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubTitle : {}", id);
        subTitleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
