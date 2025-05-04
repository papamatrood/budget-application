package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.domain.Decision;
import com.cratechnologie.budget.repository.DecisionRepository;
import com.cratechnologie.budget.service.DecisionQueryService;
import com.cratechnologie.budget.service.DecisionService;
import com.cratechnologie.budget.service.criteria.DecisionCriteria;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.Decision}.
 */
@RestController
@RequestMapping("/api/decisions")
public class DecisionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionResource.class);

    private static final String ENTITY_NAME = "decision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecisionService decisionService;

    private final DecisionRepository decisionRepository;

    private final DecisionQueryService decisionQueryService;

    public DecisionResource(
        DecisionService decisionService,
        DecisionRepository decisionRepository,
        DecisionQueryService decisionQueryService
    ) {
        this.decisionService = decisionService;
        this.decisionRepository = decisionRepository;
        this.decisionQueryService = decisionQueryService;
    }

    /**
     * {@code POST  /decisions} : Create a new decision.
     *
     * @param decision the decision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decision, or with status {@code 400 (Bad Request)} if the decision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Decision> createDecision(@Valid @RequestBody Decision decision) throws URISyntaxException {
        LOG.debug("REST request to save Decision : {}", decision);
        if (decision.getId() != null) {
            throw new BadRequestAlertException("A new decision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        decision = decisionService.save(decision);
        return ResponseEntity.created(new URI("/api/decisions/" + decision.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, decision.getId().toString()))
            .body(decision);
    }

    /**
     * {@code PUT  /decisions/:id} : Updates an existing decision.
     *
     * @param id the id of the decision to save.
     * @param decision the decision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decision,
     * or with status {@code 400 (Bad Request)} if the decision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Decision> updateDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Decision decision
    ) throws URISyntaxException {
        LOG.debug("REST request to update Decision : {}, {}", id, decision);
        if (decision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        decision = decisionService.update(decision);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decision.getId().toString()))
            .body(decision);
    }

    /**
     * {@code PATCH  /decisions/:id} : Partial updates given fields of an existing decision, field will ignore if it is null
     *
     * @param id the id of the decision to save.
     * @param decision the decision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decision,
     * or with status {@code 400 (Bad Request)} if the decision is not valid,
     * or with status {@code 404 (Not Found)} if the decision is not found,
     * or with status {@code 500 (Internal Server Error)} if the decision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Decision> partialUpdateDecision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Decision decision
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Decision partially : {}, {}", id, decision);
        if (decision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Decision> result = decisionService.partialUpdate(decision);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decision.getId().toString())
        );
    }

    /**
     * {@code GET  /decisions} : get all the decisions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decisions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Decision>> getAllDecisions(
        DecisionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Decisions by criteria: {}", criteria);

        Page<Decision> page = decisionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decisions/count} : count all the decisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDecisions(DecisionCriteria criteria) {
        LOG.debug("REST request to count Decisions by criteria: {}", criteria);
        return ResponseEntity.ok().body(decisionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /decisions/:id} : get the "id" decision.
     *
     * @param id the id of the decision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Decision> getDecision(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Decision : {}", id);
        Optional<Decision> decision = decisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decision);
    }

    /**
     * {@code DELETE  /decisions/:id} : delete the "id" decision.
     *
     * @param id the id of the decision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecision(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Decision : {}", id);
        decisionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
