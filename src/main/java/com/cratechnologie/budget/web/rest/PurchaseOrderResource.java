package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.domain.PurchaseOrder;
import com.cratechnologie.budget.repository.PurchaseOrderRepository;
import com.cratechnologie.budget.service.PurchaseOrderQueryService;
import com.cratechnologie.budget.service.PurchaseOrderService;
import com.cratechnologie.budget.service.criteria.PurchaseOrderCriteria;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.PurchaseOrder}.
 */
@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderResource.class);

    private static final String ENTITY_NAME = "purchaseOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseOrderService purchaseOrderService;

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final PurchaseOrderQueryService purchaseOrderQueryService;

    public PurchaseOrderResource(
        PurchaseOrderService purchaseOrderService,
        PurchaseOrderRepository purchaseOrderRepository,
        PurchaseOrderQueryService purchaseOrderQueryService
    ) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderQueryService = purchaseOrderQueryService;
    }

    /**
     * {@code POST  /purchase-orders} : Create a new purchaseOrder.
     *
     * @param purchaseOrder the purchaseOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseOrder, or with status {@code 400 (Bad Request)} if the purchaseOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@Valid @RequestBody PurchaseOrder purchaseOrder) throws URISyntaxException {
        LOG.debug("REST request to save PurchaseOrder : {}", purchaseOrder);
        if (purchaseOrder.getId() != null) {
            throw new BadRequestAlertException("A new purchaseOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        purchaseOrder = purchaseOrderService.save(purchaseOrder);
        return ResponseEntity.created(new URI("/api/purchase-orders/" + purchaseOrder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, purchaseOrder.getId().toString()))
            .body(purchaseOrder);
    }

    /**
     * {@code PUT  /purchase-orders/:id} : Updates an existing purchaseOrder.
     *
     * @param id the id of the purchaseOrder to save.
     * @param purchaseOrder the purchaseOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrder,
     * or with status {@code 400 (Bad Request)} if the purchaseOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchaseOrder purchaseOrder
    ) throws URISyntaxException {
        LOG.debug("REST request to update PurchaseOrder : {}, {}", id, purchaseOrder);
        if (purchaseOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        purchaseOrder = purchaseOrderService.update(purchaseOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrder.getId().toString()))
            .body(purchaseOrder);
    }

    /**
     * {@code PATCH  /purchase-orders/:id} : Partial updates given fields of an existing purchaseOrder, field will ignore if it is null
     *
     * @param id the id of the purchaseOrder to save.
     * @param purchaseOrder the purchaseOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseOrder,
     * or with status {@code 400 (Bad Request)} if the purchaseOrder is not valid,
     * or with status {@code 404 (Not Found)} if the purchaseOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchaseOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchaseOrder> partialUpdatePurchaseOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchaseOrder purchaseOrder
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PurchaseOrder partially : {}, {}", id, purchaseOrder);
        if (purchaseOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchaseOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchaseOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchaseOrder> result = purchaseOrderService.partialUpdate(purchaseOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /purchase-orders} : get all the purchaseOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders(
        PurchaseOrderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PurchaseOrders by criteria: {}", criteria);

        Page<PurchaseOrder> page = purchaseOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchase-orders/count} : count all the purchaseOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPurchaseOrders(PurchaseOrderCriteria criteria) {
        LOG.debug("REST request to count PurchaseOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(purchaseOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /purchase-orders/:id} : get the "id" purchaseOrder.
     *
     * @param id the id of the purchaseOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PurchaseOrder : {}", id);
        Optional<PurchaseOrder> purchaseOrder = purchaseOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseOrder);
    }

    /**
     * {@code DELETE  /purchase-orders/:id} : delete the "id" purchaseOrder.
     *
     * @param id the id of the purchaseOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PurchaseOrder : {}", id);
        purchaseOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
