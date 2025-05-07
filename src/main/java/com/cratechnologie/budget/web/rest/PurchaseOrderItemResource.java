package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.PurchaseOrderItemQueryService;
import com.cratechnologie.budget.service.PurchaseOrderItemService;
import com.cratechnologie.budget.service.criteria.PurchaseOrderItemCriteria;
import com.cratechnologie.budget.domain.PurchaseOrderItem;
import com.cratechnologie.budget.repository.PurchaseOrderItemRepository;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.PurchaseOrderItem}.
 */
@RestController
@RequestMapping("/api/purchase-order-items")
public class PurchaseOrderItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderItemResource.class);

    private static final String ENTITY_NAME = "purchaseOrderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderItemService purchaseOrderItemService;

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    private final PurchaseOrderItemQueryService purchaseOrderItemQueryService;

    public PurchaseOrderItemResource(
        PurchaseOrderItemService purchaseOrderItemService,
        PurchaseOrderItemRepository purchaseOrderItemRepository,
        PurchaseOrderItemQueryService purchaseOrderItemQueryService
    ) {
        this.purchaseOrderItemService = purchaseOrderItemService;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.purchaseOrderItemQueryService = purchaseOrderItemQueryService;
    }

    /**
     * {@code POST  /purchase-order-items} : Create a new purchaseOrderItem.
     *
     * @param purchaseOrderItem the purchaseOrderItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrderItem, or with status {@code 400 (Bad Request)} if the purchaseOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PurchaseOrderItem> createPurchaseOrderItem(@Valid @RequestBody PurchaseOrderItem purchaseOrderItem)
        throws URISyntaxException {
        LOG.debug("REST request to save PurchaseOrderItem : {}", purchaseOrderItem);
        if (purchaseOrderItem.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        purchaseOrderItem = purchaseOrderItemService.save(purchaseOrderItem);
        return ResponseEntity.created(new URI("/api/purchase-order-items/" + purchaseOrderItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, purchaseOrderItem.getId().toString()))
            .body(purchaseOrderItem);
    }

    /**
     * {@code PUT  /purchase-order-items/:id} : Updates an existing purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItem to save.
     * @param purchaseOrderItem the purchaseOrderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderItem,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> updatePurchaseOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchaseOrderItem purchaseOrderItem
    ) throws URISyntaxException {
        LOG.debug("REST request to update PurchaseOrderItem : {}, {}", id, purchaseOrderItem);
        if (purchaseOrderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        purchaseOrderItem = purchaseOrderItemService.update(purchaseOrderItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderItem.getId().toString()))
            .body(purchaseOrderItem);
    }

    /**
     * {@code PATCH  /purchase-order-items/:id} : Partial updates given fields of an existing purchaseOrderItem, field will ignore if it is null
     *
     * @param id the id of the purchaseOrderItem to save.
     * @param purchaseOrderItem the purchaseOrderItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrderItem,
     * or with status {@code 400 (Bad Request)} if the purchaseOrderItem is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrderItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrderItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrderItem> partialUpdatePurchaseOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchaseOrderItem purchaseOrderItem
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PurchaseOrderItem partially : {}, {}", id, purchaseOrderItem);
        if (purchaseOrderItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrderItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrderItem> result = purchaseOrderItemService.partialUpdate(purchaseOrderItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrderItem.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-order-items} : get all the purchaseOrderItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrderItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PurchaseOrderItem>> getAllPurchaseOrderItems(
        PurchaseOrderItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PurchaseOrderItems by criteria: {}", criteria);

        Page<PurchaseOrderItem> page = purchaseOrderItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-order-items/count} : count all the purchaseOrderItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPurchaseOrderItems(PurchaseOrderItemCriteria criteria) {
        LOG.debug("REST request to count PurchaseOrderItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseOrderItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchase-order-items/:id} : get the "id" purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrderItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderItem> getPurchaseOrderItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PurchaseOrderItem : {}", id);
        Optional<PurchaseOrderItem> purchaseOrderItem = purchaseOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrderItem);
    }

    /**
     * {@code DELETE  /purchase-order-items/:id} : delete the "id" purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PurchaseOrderItem : {}", id);
        purchaseOrderItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
