package com.cratechnologie.budget.web.rest;

import com.cratechnologie.budget.service.RecipeQueryService;
import com.cratechnologie.budget.service.RecipeService;
import com.cratechnologie.budget.service.criteria.RecipeCriteria;
import com.cratechnologie.budget.domain.Recipe;
import com.cratechnologie.budget.repository.RecipeRepository;
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
 * REST controller for managing {@link com.cratechnologie.budget.domain.Recipe}.
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipeService recipeService;

    private final RecipeRepository recipeRepository;

    private final RecipeQueryService recipeQueryService;

    public RecipeResource(RecipeService recipeService, RecipeRepository recipeRepository, RecipeQueryService recipeQueryService) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
        this.recipeQueryService = recipeQueryService;
    }

    /**
     * {@code POST  /recipes} : Create a new recipe.
     *
     * @param recipe the recipe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipe, or with status {@code 400 (Bad Request)} if the recipe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) throws URISyntaxException {
        LOG.debug("REST request to save Recipe : {}", recipe);
        if (recipe.getId() != null) {
            throw new BadRequestAlertException("A new recipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recipe = recipeService.save(recipe);
        return ResponseEntity.created(new URI("/api/recipes/" + recipe.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recipe.getId().toString()))
            .body(recipe);
    }

    /**
     * {@code PUT  /recipes/:id} : Updates an existing recipe.
     *
     * @param id the id of the recipe to save.
     * @param recipe the recipe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipe,
     * or with status {@code 400 (Bad Request)} if the recipe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable(value = "id", required = false) final Long id, @RequestBody Recipe recipe)
        throws URISyntaxException {
        LOG.debug("REST request to update Recipe : {}, {}", id, recipe);
        if (recipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recipe = recipeService.update(recipe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipe.getId().toString()))
            .body(recipe);
    }

    /**
     * {@code PATCH  /recipes/:id} : Partial updates given fields of an existing recipe, field will ignore if it is null
     *
     * @param id the id of the recipe to save.
     * @param recipe the recipe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipe,
     * or with status {@code 400 (Bad Request)} if the recipe is not valid,
     * or with status {@code 404 (Not Found)} if the recipe is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Recipe> partialUpdateRecipe(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Recipe recipe
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Recipe partially : {}, {}", id, recipe);
        if (recipe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Recipe> result = recipeService.partialUpdate(recipe);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recipe.getId().toString())
        );
    }

    /**
     * {@code GET  /recipes} : get all the recipes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Recipe>> getAllRecipes(
        RecipeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Recipes by criteria: {}", criteria);

        Page<Recipe> page = recipeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recipes/count} : count all the recipes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRecipes(RecipeCriteria criteria) {
        LOG.debug("REST request to count Recipes by criteria: {}", criteria);
        return ResponseEntity.ok().body(recipeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipe.
     *
     * @param id the id of the recipe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Recipe : {}", id);
        Optional<Recipe> recipe = recipeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recipe);
    }

    /**
     * {@code DELETE  /recipes/:id} : delete the "id" recipe.
     *
     * @param id the id of the recipe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Recipe : {}", id);
        recipeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
