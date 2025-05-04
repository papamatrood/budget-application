package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.domain.Mandate;
import com.cratechnologie.budget.repository.MandateRepository;
import com.cratechnologie.budget.service.MandateQueryService;
import com.cratechnologie.budget.service.MandateService;
import com.cratechnologie.budget.service.criteria.MandateCriteria;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.Mandate}.
 */
@RestController
@RequestMapping("/api/mandates")
public class MandateResource {

    private static final Logger LOG = LoggerFactory.getLogger(MandateResource.class);

    private static final String ENTITY_NAME = "mandate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MandateService mandateService;

    private final MandateRepository mandateRepository;

    private final MandateQueryService mandateQueryService;

    public MandateResource(MandateService mandateService, MandateRepository mandateRepository, MandateQueryService mandateQueryService) {
        this.mandateService = mandateService;
        this.mandateRepository = mandateRepository;
        this.mandateQueryService = mandateQueryService;
    }

    /**
     * {@code POST  /mandates} : Create a new mandate.
     *
     * @param mandate the mandate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mandate, or with status {@code 400 (Bad Request)} if the mandate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Mandate> createMandate(@Valid @RequestBody Mandate mandate) throws URISyntaxException {
        LOG.debug("REST request to save Mandate : {}", mandate);
        if (mandate.getId() != null) {
            throw new BadRequestAlertException("A new mandate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mandate = mandateService.save(mandate);
        return ResponseEntity.created(new URI("/api/mandates/" + mandate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mandate.getId().toString()))
            .body(mandate);
    }

    /**
     * {@code PUT  /mandates/:id} : Updates an existing mandate.
     *
     * @param id the id of the mandate to save.
     * @param mandate the mandate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mandate,
     * or with status {@code 400 (Bad Request)} if the mandate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mandate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mandate> updateMandate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mandate mandate
    ) throws URISyntaxException {
        LOG.debug("REST request to update Mandate : {}, {}", id, mandate);
        if (mandate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mandate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mandateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mandate = mandateService.update(mandate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mandate.getId().toString()))
            .body(mandate);
    }

    /**
     * {@code PATCH  /mandates/:id} : Partial updates given fields of an existing mandate, field will ignore if it is null
     *
     * @param id the id of the mandate to save.
     * @param mandate the mandate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mandate,
     * or with status {@code 400 (Bad Request)} if the mandate is not valid,
     * or with status {@code 404 (Not Found)} if the mandate is not found,
     * or with status {@code 500 (Internal Server Error)} if the mandate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mandate> partialUpdateMandate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mandate mandate
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Mandate partially : {}, {}", id, mandate);
        if (mandate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mandate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mandateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mandate> result = mandateService.partialUpdate(mandate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mandate.getId().toString())
        );
    }

    /**
     * {@code GET  /mandates} : get all the mandates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mandates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Mandate>> getAllMandates(
        MandateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Mandates by criteria: {}", criteria);

        Page<Mandate> page = mandateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mandates/count} : count all the mandates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMandates(MandateCriteria criteria) {
        LOG.debug("REST request to count Mandates by criteria: {}", criteria);
        return ResponseEntity.ok().body(mandateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mandates/:id} : get the "id" mandate.
     *
     * @param id the id of the mandate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mandate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mandate> getMandate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Mandate : {}", id);
        Optional<Mandate> mandate = mandateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mandate);
    }

    /**
     * {@code DELETE  /mandates/:id} : delete the "id" mandate.
     *
     * @param id the id of the mandate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMandate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Mandate : {}", id);
        mandateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
