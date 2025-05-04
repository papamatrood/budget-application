package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.repository.FinancialYearRepository;
import com.cratechnologie.budget.service.FinancialYearQueryService;
import com.cratechnologie.budget.service.FinancialYearService;
import com.cratechnologie.budget.service.criteria.FinancialYearCriteria;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.FinancialYear}.
 */
@RestController
@RequestMapping("/api/financial-years")
public class FinancialYearResource {

    private static final Logger LOG = LoggerFactory.getLogger(FinancialYearResource.class);

    private static final String ENTITY_NAME = "financialYear";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialYearService financialYearService;

    private final FinancialYearRepository financialYearRepository;

    private final FinancialYearQueryService financialYearQueryService;

    public FinancialYearResource(
        FinancialYearService financialYearService,
        FinancialYearRepository financialYearRepository,
        FinancialYearQueryService financialYearQueryService
    ) {
        this.financialYearService = financialYearService;
        this.financialYearRepository = financialYearRepository;
        this.financialYearQueryService = financialYearQueryService;
    }

    /**
     * {@code POST  /financial-years} : Create a new financialYear.
     *
     * @param financialYear the financialYear to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialYear, or with status {@code 400 (Bad Request)} if the financialYear has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FinancialYear> createFinancialYear(@Valid @RequestBody FinancialYear financialYear) throws URISyntaxException {
        LOG.debug("REST request to save FinancialYear : {}", financialYear);
        if (financialYear.getId() != null) {
            throw new BadRequestAlertException("A new financialYear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financialYear = financialYearService.save(financialYear);
        return ResponseEntity.created(new URI("/api/financial-years/" + financialYear.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, financialYear.getId().toString()))
            .body(financialYear);
    }

    /**
     * {@code PUT  /financial-years/:id} : Updates an existing financialYear.
     *
     * @param id the id of the financialYear to save.
     * @param financialYear the financialYear to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialYear,
     * or with status {@code 400 (Bad Request)} if the financialYear is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialYear couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FinancialYear> updateFinancialYear(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinancialYear financialYear
    ) throws URISyntaxException {
        LOG.debug("REST request to update FinancialYear : {}, {}", id, financialYear);
        if (financialYear.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialYear.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialYearRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financialYear = financialYearService.update(financialYear);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialYear.getId().toString()))
            .body(financialYear);
    }

    /**
     * {@code PATCH  /financial-years/:id} : Partial updates given fields of an existing financialYear, field will ignore if it is null
     *
     * @param id the id of the financialYear to save.
     * @param financialYear the financialYear to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialYear,
     * or with status {@code 400 (Bad Request)} if the financialYear is not valid,
     * or with status {@code 404 (Not Found)} if the financialYear is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialYear couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinancialYear> partialUpdateFinancialYear(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinancialYear financialYear
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FinancialYear partially : {}, {}", id, financialYear);
        if (financialYear.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialYear.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialYearRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinancialYear> result = financialYearService.partialUpdate(financialYear);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialYear.getId().toString())
        );
    }

    /**
     * {@code GET  /financial-years} : get all the financialYears.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financialYears in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FinancialYear>> getAllFinancialYears(
        FinancialYearCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get FinancialYears by criteria: {}", criteria);

        Page<FinancialYear> page = financialYearQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financial-years/count} : count all the financialYears.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFinancialYears(FinancialYearCriteria criteria) {
        LOG.debug("REST request to count FinancialYears by criteria: {}", criteria);
        return ResponseEntity.ok().body(financialYearQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /financial-years/:id} : get the "id" financialYear.
     *
     * @param id the id of the financialYear to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialYear, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FinancialYear> getFinancialYear(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FinancialYear : {}", id);
        Optional<FinancialYear> financialYear = financialYearService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialYear);
    }

    /**
     * {@code DELETE  /financial-years/:id} : delete the "id" financialYear.
     *
     * @param id the id of the financialYear to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialYear(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FinancialYear : {}", id);
        financialYearService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
