package com.gpm.project.web.rest;

import com.gpm.project.repository.AffaireSocieteAdjRepository;
import com.gpm.project.service.AffaireSocieteAdjService;
import com.gpm.project.service.dto.AffaireSocieteAdjDTO;
import com.gpm.project.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gpm.project.domain.AffaireSocieteAdj}.
 */
@RestController
@RequestMapping("/api")
public class AffaireSocieteAdjResource {

    private final Logger log = LoggerFactory.getLogger(AffaireSocieteAdjResource.class);

    private static final String ENTITY_NAME = "projectServiceAffaireSocieteAdj";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffaireSocieteAdjService affaireSocieteAdjService;

    private final AffaireSocieteAdjRepository affaireSocieteAdjRepository;

    public AffaireSocieteAdjResource(
        AffaireSocieteAdjService affaireSocieteAdjService,
        AffaireSocieteAdjRepository affaireSocieteAdjRepository
    ) {
        this.affaireSocieteAdjService = affaireSocieteAdjService;
        this.affaireSocieteAdjRepository = affaireSocieteAdjRepository;
    }

    /**
     * {@code POST  /affaire-societe-adjs} : Create a new affaireSocieteAdj.
     *
     * @param affaireSocieteAdjDTO the affaireSocieteAdjDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affaireSocieteAdjDTO, or with status {@code 400 (Bad Request)} if the affaireSocieteAdj has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affaire-societe-adjs")
    public ResponseEntity<AffaireSocieteAdjDTO> createAffaireSocieteAdj(@RequestBody AffaireSocieteAdjDTO affaireSocieteAdjDTO)
        throws URISyntaxException {
        log.debug("REST request to save AffaireSocieteAdj : {}", affaireSocieteAdjDTO);
        if (affaireSocieteAdjDTO.getId() != null) {
            throw new BadRequestAlertException("A new affaireSocieteAdj cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AffaireSocieteAdjDTO result = affaireSocieteAdjService.save(affaireSocieteAdjDTO);
        return ResponseEntity
            .created(new URI("/api/affaire-societe-adjs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affaire-societe-adjs/:id} : Updates an existing affaireSocieteAdj.
     *
     * @param id the id of the affaireSocieteAdjDTO to save.
     * @param affaireSocieteAdjDTO the affaireSocieteAdjDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaireSocieteAdjDTO,
     * or with status {@code 400 (Bad Request)} if the affaireSocieteAdjDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affaireSocieteAdjDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affaire-societe-adjs/{id}")
    public ResponseEntity<AffaireSocieteAdjDTO> updateAffaireSocieteAdj(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AffaireSocieteAdjDTO affaireSocieteAdjDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AffaireSocieteAdj : {}, {}", id, affaireSocieteAdjDTO);
        if (affaireSocieteAdjDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaireSocieteAdjDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireSocieteAdjRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AffaireSocieteAdjDTO result = affaireSocieteAdjService.update(affaireSocieteAdjDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaireSocieteAdjDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affaire-societe-adjs/:id} : Partial updates given fields of an existing affaireSocieteAdj, field will ignore if it is null
     *
     * @param id the id of the affaireSocieteAdjDTO to save.
     * @param affaireSocieteAdjDTO the affaireSocieteAdjDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affaireSocieteAdjDTO,
     * or with status {@code 400 (Bad Request)} if the affaireSocieteAdjDTO is not valid,
     * or with status {@code 404 (Not Found)} if the affaireSocieteAdjDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the affaireSocieteAdjDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affaire-societe-adjs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffaireSocieteAdjDTO> partialUpdateAffaireSocieteAdj(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AffaireSocieteAdjDTO affaireSocieteAdjDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AffaireSocieteAdj partially : {}, {}", id, affaireSocieteAdjDTO);
        if (affaireSocieteAdjDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affaireSocieteAdjDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affaireSocieteAdjRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffaireSocieteAdjDTO> result = affaireSocieteAdjService.partialUpdate(affaireSocieteAdjDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affaireSocieteAdjDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /affaire-societe-adjs} : get all the affaireSocieteAdjs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affaireSocieteAdjs in body.
     */
    @GetMapping("/affaire-societe-adjs")
    public List<AffaireSocieteAdjDTO> getAllAffaireSocieteAdjs() {
        log.debug("REST request to get all AffaireSocieteAdjs");
        return affaireSocieteAdjService.findAll();
    }

    /**
     * {@code GET  /affaire-societe-adjs/:id} : get the "id" affaireSocieteAdj.
     *
     * @param id the id of the affaireSocieteAdjDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affaireSocieteAdjDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affaire-societe-adjs/{id}")
    public ResponseEntity<AffaireSocieteAdjDTO> getAffaireSocieteAdj(@PathVariable Long id) {
        log.debug("REST request to get AffaireSocieteAdj : {}", id);
        Optional<AffaireSocieteAdjDTO> affaireSocieteAdjDTO = affaireSocieteAdjService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affaireSocieteAdjDTO);
    }

    /**
     * {@code DELETE  /affaire-societe-adjs/:id} : delete the "id" affaireSocieteAdj.
     *
     * @param id the id of the affaireSocieteAdjDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affaire-societe-adjs/{id}")
    public ResponseEntity<Void> deleteAffaireSocieteAdj(@PathVariable Long id) {
        log.debug("REST request to delete AffaireSocieteAdj : {}", id);
        affaireSocieteAdjService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
