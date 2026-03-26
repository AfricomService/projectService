package com.gpm.project.web.rest;

import com.gpm.project.repository.AffaireArticleRepository;
import com.gpm.project.service.AffaireArticleService;
import com.gpm.project.service.dto.AffaireArticleDTO;
import com.gpm.project.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gpm.project.domain.AffaireArticle}.
 */
@RestController
@RequestMapping("/api")
public class AffaireArticleResource {

    private final Logger log = LoggerFactory.getLogger(AffaireArticleResource.class);

    private static final String ENTITY_NAME = "projectServiceAffaireArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffaireArticleService affaireArticleService;

    private final AffaireArticleRepository affaireArticleRepository;

    public AffaireArticleResource(AffaireArticleService affaireArticleService, AffaireArticleRepository affaireArticleRepository) {
        this.affaireArticleService = affaireArticleService;
        this.affaireArticleRepository = affaireArticleRepository;
    }

    /**
     * {@code POST  /affaire-articles} : Create a new affaireArticle.
     *
     * @param affaireArticleDTO the affaireArticleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affaireArticleDTO, or with status {@code 400 (Bad Request)} if the affaireArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affaire-articles")
    public ResponseEntity<AffaireArticleDTO> createAffaireArticle(@Valid @RequestBody AffaireArticleDTO affaireArticleDTO)
        throws URISyntaxException {
        log.debug("REST request to save AffaireArticle : {}", affaireArticleDTO);
        if (affaireArticleDTO.getId() != null) {
            throw new BadRequestAlertException("A new affaireArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AffaireArticleDTO result = affaireArticleService.save(affaireArticleDTO);
        return ResponseEntity
            .created(new URI("/api/affaire-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affaire-articles/:id} : Updates an existing affaireArticle.
     *
     * @param id the id of the affaireArticleDTO to save.
     * @param affaireArticleDTO the affaireArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaireArticleDTO,
     * or with status {@code 400 (Bad Request)} if the affaireArticleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affaireArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affaire-articles/{id}")
    public ResponseEntity<AffaireArticleDTO> updateAffaireArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AffaireArticleDTO affaireArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AffaireArticle : {}, {}", id, affaireArticleDTO);
        if (affaireArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaireArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AffaireArticleDTO result = affaireArticleService.update(affaireArticleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaireArticleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affaire-articles/:id} : Partial updates given fields of an existing affaireArticle, field will ignore if it is null
     *
     * @param id the id of the affaireArticleDTO to save.
     * @param affaireArticleDTO the affaireArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaireArticleDTO,
     * or with status {@code 400 (Bad Request)} if the affaireArticleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affaireArticleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affaireArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affaire-articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffaireArticleDTO> partialUpdateAffaireArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AffaireArticleDTO affaireArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AffaireArticle partially : {}, {}", id, affaireArticleDTO);
        if (affaireArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaireArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffaireArticleDTO> result = affaireArticleService.partialUpdate(affaireArticleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaireArticleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affaire-articles} : get all the affaireArticles.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affaireArticles in body.
     */
    @GetMapping("/affaire-articles")
    public ResponseEntity<List<AffaireArticleDTO>> getAllAffaireArticles(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of AffaireArticles");
        Page<AffaireArticleDTO> page;
        if (eagerload) {
            page = affaireArticleService.findAllWithEagerRelationships(pageable);
        } else {
            page = affaireArticleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affaire-articles/:id} : get the "id" affaireArticle.
     *
     * @param id the id of the affaireArticleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affaireArticleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affaire-articles/{id}")
    public ResponseEntity<AffaireArticleDTO> getAffaireArticle(@PathVariable Long id) {
        log.debug("REST request to get AffaireArticle : {}", id);
        Optional<AffaireArticleDTO> affaireArticleDTO = affaireArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affaireArticleDTO);
    }

    /**
     * {@code DELETE  /affaire-articles/:id} : delete the "id" affaireArticle.
     *
     * @param id the id of the affaireArticleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affaire-articles/{id}")
    public ResponseEntity<Void> deleteAffaireArticle(@PathVariable Long id) {
        log.debug("REST request to delete AffaireArticle : {}", id);
        affaireArticleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /affaire-articles/affaire/:affaireId} : get all the affaireArticles by affaire id.
     *
     * @param affaireId the id of the affaire.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affaireArticles in body.
     */
    @GetMapping("/affaire-articles/affaire/{affaireId}")
    public ResponseEntity<List<AffaireArticleDTO>> getAffaireArticlesByAffaireId(@PathVariable Long affaireId) {
        log.debug("REST request to get AffaireArticles by affaireId : {}", affaireId);
        List<AffaireArticleDTO> result = affaireArticleService.findByAffaireId(affaireId);
        return ResponseEntity.ok().body(result);
    }
}
