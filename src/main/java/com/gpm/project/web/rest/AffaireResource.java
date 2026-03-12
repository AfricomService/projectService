package com.gpm.project.web.rest;

import com.gpm.project.repository.AffaireRepository;
import com.gpm.project.service.AffaireService;
import com.gpm.project.service.dto.AffaireDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.Affaire}.
 */
@RestController
@RequestMapping("/api")
public class AffaireResource {

    private final Logger log = LoggerFactory.getLogger(AffaireResource.class);

    private static final String ENTITY_NAME = "projectServiceAffaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffaireService affaireService;

    private final AffaireRepository affaireRepository;

    public AffaireResource(AffaireService affaireService, AffaireRepository affaireRepository) {
        this.affaireService = affaireService;
        this.affaireRepository = affaireRepository;
    }

    /**
     * {@code POST  /affaires} : Create a new affaire.
     *
     * @param affaireDTO the affaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affaireDTO, or with status {@code 400 (Bad Request)} if the affaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affaires")
    public ResponseEntity<AffaireDTO> createAffaire(@Valid @RequestBody AffaireDTO affaireDTO) throws URISyntaxException {
        log.debug("REST request to save Affaire : {}", affaireDTO);
        if (affaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new affaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AffaireDTO result = affaireService.save(affaireDTO);
        return ResponseEntity
            .created(new URI("/api/affaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affaires/:id} : Updates an existing affaire.
     *
     * @param id the id of the affaireDTO to save.
     * @param affaireDTO the affaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaireDTO,
     * or with status {@code 400 (Bad Request)} if the affaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affaires/{id}")
    public ResponseEntity<AffaireDTO> updateAffaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AffaireDTO affaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Affaire : {}, {}", id, affaireDTO);
        if (affaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AffaireDTO result = affaireService.update(affaireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affaires/:id} : Partial updates given fields of an existing affaire, field will ignore if it is null
     *
     * @param id the id of the affaireDTO to save.
     * @param affaireDTO the affaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaireDTO,
     * or with status {@code 400 (Bad Request)} if the affaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffaireDTO> partialUpdateAffaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AffaireDTO affaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Affaire partially : {}, {}", id, affaireDTO);
        if (affaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffaireDTO> result = affaireService.partialUpdate(affaireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affaires} : get all the affaires.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affaires in body.
     */
    @GetMapping("/affaires")
    public ResponseEntity<List<AffaireDTO>> getAllAffaires(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Affaires");
        Page<AffaireDTO> page;
        if (eagerload) {
            page = affaireService.findAllWithEagerRelationships(pageable);
        } else {
            page = affaireService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /affaires/:id} : get the "id" affaire.
     *
     * @param id the id of the affaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affaires/{id}")
    public ResponseEntity<AffaireDTO> getAffaire(@PathVariable Long id) {
        log.debug("REST request to get Affaire : {}", id);
        Optional<AffaireDTO> affaireDTO = affaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affaireDTO);
    }

    /**
     * {@code DELETE  /affaires/:id} : delete the "id" affaire.
     *
     * @param id the id of the affaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affaires/{id}")
    public ResponseEntity<Void> deleteAffaire(@PathVariable Long id) {
        log.debug("REST request to delete Affaire : {}", id);
        affaireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
