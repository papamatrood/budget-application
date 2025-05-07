package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.EngagementQueryService;
import com.cratechnologie.budget.service.EngagementService;
import com.cratechnologie.budget.service.criteria.EngagementCriteria;
import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.repository.EngagementRepository;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.Engagement}.
 */
@RestController
@RequestMapping("/api/engagements")
public class EngagementResource {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementResource.class);

    private static final String ENTITY_NAME = "engagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EngagementService engagementService;

    private final EngagementRepository engagementRepository;

    private final EngagementQueryService engagementQueryService;

    public EngagementResource(
        EngagementService engagementService,
        EngagementRepository engagementRepository,
        EngagementQueryService engagementQueryService
    ) {
        this.engagementService = engagementService;
        this.engagementRepository = engagementRepository;
        this.engagementQueryService = engagementQueryService;
    }

    /**
     * {@code POST  /engagements} : Create a new engagement.
     *
     * @param engagement the engagement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new engagement, or with status {@code 400 (Bad Request)} if the engagement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Engagement> createEngagement(@Valid @RequestBody Engagement engagement) throws URISyntaxException {
        LOG.debug("REST request to save Engagement : {}", engagement);
        if (engagement.getId() != null) {
            throw new BadRequestAlertException("A new engagement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        engagement = engagementService.save(engagement);
        return ResponseEntity.created(new URI("/api/engagements/" + engagement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, engagement.getId().toString()))
            .body(engagement);
    }

    /**
     * {@code PUT  /engagements/:id} : Updates an existing engagement.
     *
     * @param id the id of the engagement to save.
     * @param engagement the engagement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated engagement,
     * or with status {@code 400 (Bad Request)} if the engagement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the engagement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Engagement> updateEngagement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Engagement engagement
    ) throws URISyntaxException {
        LOG.debug("REST request to update Engagement : {}, {}", id, engagement);
        if (engagement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, engagement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!engagementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        engagement = engagementService.update(engagement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, engagement.getId().toString()))
            .body(engagement);
    }

    /**
     * {@code PATCH  /engagements/:id} : Partial updates given fields of an existing engagement, field will ignore if it is null
     *
     * @param id the id of the engagement to save.
     * @param engagement the engagement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated engagement,
     * or with status {@code 400 (Bad Request)} if the engagement is not valid,
     * or with status {@code 404 (Not Found)} if the engagement is not found,
     * or with status {@code 500 (Internal Server Error)} if the engagement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Engagement> partialUpdateEngagement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Engagement engagement
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Engagement partially : {}, {}", id, engagement);
        if (engagement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, engagement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!engagementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Engagement> result = engagementService.partialUpdate(engagement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, engagement.getId().toString())
        );
    }

    /**
     * {@code GET  /engagements} : get all the engagements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of engagements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Engagement>> getAllEngagements(
        EngagementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Engagements by criteria: {}", criteria);

        Page<Engagement> page = engagementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /engagements/count} : count all the engagements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEngagements(EngagementCriteria criteria) {
        LOG.debug("REST request to count Engagements by criteria: {}", criteria);
        return ResponseEntity.ok().body(engagementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /engagements/:id} : get the "id" engagement.
     *
     * @param id the id of the engagement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the engagement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Engagement> getEngagement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Engagement : {}", id);
        Optional<Engagement> engagement = engagementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(engagement);
    }

    /**
     * {@code DELETE  /engagements/:id} : delete the "id" engagement.
     *
     * @param id the id of the engagement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEngagement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Engagement : {}", id);
        engagementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
