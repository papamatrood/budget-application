package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.DecisionItemQueryService;
import com.cratechnologie.budget.service.DecisionItemService;
import com.cratechnologie.budget.service.criteria.DecisionItemCriteria;
import com.cratechnologie.budget.domain.DecisionItem;
import com.cratechnologie.budget.repository.DecisionItemRepository;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.DecisionItem}.
 */
@RestController
@RequestMapping("/api/decision-items")
public class DecisionItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionItemResource.class);

    private static final String ENTITY_NAME = "decisionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecisionItemService decisionItemService;

    private final DecisionItemRepository decisionItemRepository;

    private final DecisionItemQueryService decisionItemQueryService;

    public DecisionItemResource(
        DecisionItemService decisionItemService,
        DecisionItemRepository decisionItemRepository,
        DecisionItemQueryService decisionItemQueryService
    ) {
        this.decisionItemService = decisionItemService;
        this.decisionItemRepository = decisionItemRepository;
        this.decisionItemQueryService = decisionItemQueryService;
    }

    /**
     * {@code POST  /decision-items} : Create a new decisionItem.
     *
     * @param decisionItem the decisionItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decisionItem, or with status {@code 400 (Bad Request)} if the decisionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DecisionItem> createDecisionItem(@Valid @RequestBody DecisionItem decisionItem) throws URISyntaxException {
        LOG.debug("REST request to save DecisionItem : {}", decisionItem);
        if (decisionItem.getId() != null) {
            throw new BadRequestAlertException("A new decisionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        decisionItem = decisionItemService.save(decisionItem);
        return ResponseEntity.created(new URI("/api/decision-items/" + decisionItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, decisionItem.getId().toString()))
            .body(decisionItem);
    }

    /**
     * {@code PUT  /decision-items/:id} : Updates an existing decisionItem.
     *
     * @param id the id of the decisionItem to save.
     * @param decisionItem the decisionItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionItem,
     * or with status {@code 400 (Bad Request)} if the decisionItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decisionItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DecisionItem> updateDecisionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DecisionItem decisionItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update DecisionItem : {}, {}", id, decisionItem);
        if (decisionItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decisionItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        decisionItem = decisionItemService.update(decisionItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionItem.getId().toString()))
            .body(decisionItem);
    }

    /**
     * {@code PATCH  /decision-items/:id} : Partial updates given fields of an existing decisionItem, field will ignore if it is null
     *
     * @param id the id of the decisionItem to save.
     * @param decisionItem the decisionItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionItem,
     * or with status {@code 400 (Bad Request)} if the decisionItem is not valid,
     * or with status {@code 404 (Not Found)} if the decisionItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the decisionItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DecisionItem> partialUpdateDecisionItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DecisionItem decisionItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DecisionItem partially : {}, {}", id, decisionItem);
        if (decisionItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, decisionItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!decisionItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DecisionItem> result = decisionItemService.partialUpdate(decisionItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionItem.getId().toString())
        );
    }

    /**
     * {@code GET  /decision-items} : get all the decisionItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decisionItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DecisionItem>> getAllDecisionItems(
        DecisionItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DecisionItems by criteria: {}", criteria);

        Page<DecisionItem> page = decisionItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decision-items/count} : count all the decisionItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDecisionItems(DecisionItemCriteria criteria) {
        LOG.debug("REST request to count DecisionItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(decisionItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /decision-items/:id} : get the "id" decisionItem.
     *
     * @param id the id of the decisionItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decisionItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DecisionItem> getDecisionItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DecisionItem : {}", id);
        Optional<DecisionItem> decisionItem = decisionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decisionItem);
    }

    /**
     * {@code DELETE  /decision-items/:id} : delete the "id" decisionItem.
     *
     * @param id the id of the decisionItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDecisionItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DecisionItem : {}", id);
        decisionItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
