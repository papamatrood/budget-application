package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.AnnexDecisionQueryService;
import com.cratechnologie.budget.service.AnnexDecisionService;
import com.cratechnologie.budget.service.criteria.AnnexDecisionCriteria;
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.repository.AnnexDecisionRepository;
import com.cratechnologie.budget.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.AnnexDecision}.
 */
@RestController
@RequestMapping("/api/annex-decisions")
public class AnnexDecisionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnnexDecisionResource.class);

    private static final String ENTITY_NAME = "annexDecision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnexDecisionService annexDecisionService;

    private final AnnexDecisionRepository annexDecisionRepository;

    private final AnnexDecisionQueryService annexDecisionQueryService;

    public AnnexDecisionResource(
        AnnexDecisionService annexDecisionService,
        AnnexDecisionRepository annexDecisionRepository,
        AnnexDecisionQueryService annexDecisionQueryService
    ) {
        this.annexDecisionService = annexDecisionService;
        this.annexDecisionRepository = annexDecisionRepository;
        this.annexDecisionQueryService = annexDecisionQueryService;
    }

    /**
     * {@code POST  /annex-decisions} : Create a new annexDecision.
     *
     * @param annexDecision the annexDecision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annexDecision, or with status {@code 400 (Bad Request)} if the annexDecision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AnnexDecision> createAnnexDecision(@RequestBody AnnexDecision annexDecision) throws URISyntaxException {
        LOG.debug("REST request to save AnnexDecision : {}", annexDecision);
        if (annexDecision.getId() != null) {
            throw new BadRequestAlertException("A new annexDecision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        annexDecision = annexDecisionService.save(annexDecision);
        return ResponseEntity.created(new URI("/api/annex-decisions/" + annexDecision.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, annexDecision.getId().toString()))
            .body(annexDecision);
    }

    /**
     * {@code PUT  /annex-decisions/:id} : Updates an existing annexDecision.
     *
     * @param id the id of the annexDecision to save.
     * @param annexDecision the annexDecision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexDecision,
     * or with status {@code 400 (Bad Request)} if the annexDecision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annexDecision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnnexDecision> updateAnnexDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnexDecision annexDecision
    ) throws URISyntaxException {
        LOG.debug("REST request to update AnnexDecision : {}, {}", id, annexDecision);
        if (annexDecision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexDecision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexDecisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        annexDecision = annexDecisionService.update(annexDecision);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annexDecision.getId().toString()))
            .body(annexDecision);
    }

    /**
     * {@code PATCH  /annex-decisions/:id} : Partial updates given fields of an existing annexDecision, field will ignore if it is null
     *
     * @param id the id of the annexDecision to save.
     * @param annexDecision the annexDecision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annexDecision,
     * or with status {@code 400 (Bad Request)} if the annexDecision is not valid,
     * or with status {@code 404 (Not Found)} if the annexDecision is not found,
     * or with status {@code 500 (Internal Server Error)} if the annexDecision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnnexDecision> partialUpdateAnnexDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnnexDecision annexDecision
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AnnexDecision partially : {}, {}", id, annexDecision);
        if (annexDecision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annexDecision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annexDecisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnexDecision> result = annexDecisionService.partialUpdate(annexDecision);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annexDecision.getId().toString())
        );
    }

    /**
     * {@code GET  /annex-decisions} : get all the annexDecisions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annexDecisions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AnnexDecision>> getAllAnnexDecisions(
        AnnexDecisionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AnnexDecisions by criteria: {}", criteria);

        Page<AnnexDecision> page = annexDecisionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annex-decisions/count} : count all the annexDecisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAnnexDecisions(AnnexDecisionCriteria criteria) {
        LOG.debug("REST request to count AnnexDecisions by criteria: {}", criteria);
        return ResponseEntity.ok().body(annexDecisionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /annex-decisions/:id} : get the "id" annexDecision.
     *
     * @param id the id of the annexDecision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annexDecision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnexDecision> getAnnexDecision(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AnnexDecision : {}", id);
        Optional<AnnexDecision> annexDecision = annexDecisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annexDecision);
    }

    /**
     * {@code DELETE  /annex-decisions/:id} : delete the "id" annexDecision.
     *
     * @param id the id of the annexDecision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnexDecision(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AnnexDecision : {}", id);
        annexDecisionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
