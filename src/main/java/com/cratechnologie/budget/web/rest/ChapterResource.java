package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.ChapterQueryService;
import com.cratechnologie.budget.service.ChapterService;
import com.cratechnologie.budget.service.criteria.ChapterCriteria;
import com.cratechnologie.budget.domain.Chapter;
import com.cratechnologie.budget.repository.ChapterRepository;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.Chapter}.
 */
@RestController
@RequestMapping("/api/chapters")
public class ChapterResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChapterResource.class);

    private static final String ENTITY_NAME = "chapter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChapterService chapterService;

    private final ChapterRepository chapterRepository;

    private final ChapterQueryService chapterQueryService;

    public ChapterResource(ChapterService chapterService, ChapterRepository chapterRepository, ChapterQueryService chapterQueryService) {
        this.chapterService = chapterService;
        this.chapterRepository = chapterRepository;
        this.chapterQueryService = chapterQueryService;
    }

    /**
     * {@code POST  /chapters} : Create a new chapter.
     *
     * @param chapter the chapter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chapter, or with status {@code 400 (Bad Request)} if the chapter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Chapter> createChapter(@Valid @RequestBody Chapter chapter) throws URISyntaxException {
        LOG.debug("REST request to save Chapter : {}", chapter);
        if (chapter.getId() != null) {
            throw new BadRequestAlertException("A new chapter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chapter = chapterService.save(chapter);
        return ResponseEntity.created(new URI("/api/chapters/" + chapter.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chapter.getId().toString()))
            .body(chapter);
    }

    /**
     * {@code PUT  /chapters/:id} : Updates an existing chapter.
     *
     * @param id the id of the chapter to save.
     * @param chapter the chapter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chapter,
     * or with status {@code 400 (Bad Request)} if the chapter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chapter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Chapter> updateChapter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Chapter chapter
    ) throws URISyntaxException {
        LOG.debug("REST request to update Chapter : {}, {}", id, chapter);
        if (chapter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chapter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chapterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chapter = chapterService.update(chapter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chapter.getId().toString()))
            .body(chapter);
    }

    /**
     * {@code PATCH  /chapters/:id} : Partial updates given fields of an existing chapter, field will ignore if it is null
     *
     * @param id the id of the chapter to save.
     * @param chapter the chapter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chapter,
     * or with status {@code 400 (Bad Request)} if the chapter is not valid,
     * or with status {@code 404 (Not Found)} if the chapter is not found,
     * or with status {@code 500 (Internal Server Error)} if the chapter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Chapter> partialUpdateChapter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Chapter chapter
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Chapter partially : {}, {}", id, chapter);
        if (chapter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chapter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chapterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Chapter> result = chapterService.partialUpdate(chapter);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chapter.getId().toString())
        );
    }

    /**
     * {@code GET  /chapters} : get all the chapters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chapters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Chapter>> getAllChapters(
        ChapterCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Chapters by criteria: {}", criteria);

        Page<Chapter> page = chapterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chapters/count} : count all the chapters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countChapters(ChapterCriteria criteria) {
        LOG.debug("REST request to count Chapters by criteria: {}", criteria);
        return ResponseEntity.ok().body(chapterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chapters/:id} : get the "id" chapter.
     *
     * @param id the id of the chapter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chapter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapter(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Chapter : {}", id);
        Optional<Chapter> chapter = chapterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chapter);
    }

    /**
     * {@code DELETE  /chapters/:id} : delete the "id" chapter.
     *
     * @param id the id of the chapter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Chapter : {}", id);
        chapterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
