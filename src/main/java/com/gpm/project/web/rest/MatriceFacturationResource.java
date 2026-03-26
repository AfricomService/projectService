package com.gpm.project.web.rest;

import com.gpm.project.repository.MatriceFacturationRepository;
import com.gpm.project.service.MatriceFacturationService;
import com.gpm.project.service.dto.MatriceFacturationDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.MatriceFacturation}.
 */
@RestController
@RequestMapping("/api")
public class MatriceFacturationResource {

    private final Logger log = LoggerFactory.getLogger(MatriceFacturationResource.class);

    private static final String ENTITY_NAME = "projectServiceMatriceFacturation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatriceFacturationService matriceFacturationService;

    private final MatriceFacturationRepository matriceFacturationRepository;

    public MatriceFacturationResource(
        MatriceFacturationService matriceFacturationService,
        MatriceFacturationRepository matriceFacturationRepository
    ) {
        this.matriceFacturationService = matriceFacturationService;
        this.matriceFacturationRepository = matriceFacturationRepository;
    }

    /**
     * {@code POST  /matrice-facturations} : Create a new matriceFacturation.
     *
     * @param matriceFacturationDTO the matriceFacturationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matriceFacturationDTO, or with status {@code 400 (Bad Request)} if the matriceFacturation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/matrice-facturations")
    public ResponseEntity<MatriceFacturationDTO> createMatriceFacturation(@Valid @RequestBody MatriceFacturationDTO matriceFacturationDTO)
        throws URISyntaxException {
        log.debug("REST request to save MatriceFacturation : {}", matriceFacturationDTO);
        if (matriceFacturationDTO.getId() != null) {
            throw new BadRequestAlertException("A new matriceFacturation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatriceFacturationDTO result = matriceFacturationService.save(matriceFacturationDTO);
        return ResponseEntity
            .created(new URI("/api/matrice-facturations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /matrice-facturations/:id} : Updates an existing matriceFacturation.
     *
     * @param id the id of the matriceFacturationDTO to save.
     * @param matriceFacturationDTO the matriceFacturationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matriceFacturationDTO,
     * or with status {@code 400 (Bad Request)} if the matriceFacturationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matriceFacturationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/matrice-facturations/{id}")
    public ResponseEntity<MatriceFacturationDTO> updateMatriceFacturation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatriceFacturationDTO matriceFacturationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MatriceFacturation : {}, {}", id, matriceFacturationDTO);
        if (matriceFacturationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matriceFacturationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriceFacturationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MatriceFacturationDTO result = matriceFacturationService.update(matriceFacturationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matriceFacturationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /matrice-facturations/:id} : Partial updates given fields of an existing matriceFacturation, field will ignore if it is null
     *
     * @param id the id of the matriceFacturationDTO to save.
     * @param matriceFacturationDTO the matriceFacturationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matriceFacturationDTO,
     * or with status {@code 400 (Bad Request)} if the matriceFacturationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matriceFacturationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matriceFacturationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/matrice-facturations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatriceFacturationDTO> partialUpdateMatriceFacturation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatriceFacturationDTO matriceFacturationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MatriceFacturation partially : {}, {}", id, matriceFacturationDTO);
        if (matriceFacturationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matriceFacturationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriceFacturationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatriceFacturationDTO> result = matriceFacturationService.partialUpdate(matriceFacturationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matriceFacturationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /matrice-facturations} : get all the matriceFacturations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matriceFacturations in body.
     */
    @GetMapping("/matrice-facturations")
    public ResponseEntity<List<MatriceFacturationDTO>> getAllMatriceFacturations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of MatriceFacturations");
        Page<MatriceFacturationDTO> page;
        if (eagerload) {
            page = matriceFacturationService.findAllWithEagerRelationships(pageable);
        } else {
            page = matriceFacturationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matrice-facturations/:id} : get the "id" matriceFacturation.
     *
     * @param id the id of the matriceFacturationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matriceFacturationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/matrice-facturations/{id}")
    public ResponseEntity<MatriceFacturationDTO> getMatriceFacturation(@PathVariable Long id) {
        log.debug("REST request to get MatriceFacturation : {}", id);
        Optional<MatriceFacturationDTO> matriceFacturationDTO = matriceFacturationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matriceFacturationDTO);
    }

    /**
     * {@code GET  /matrice-facturations/affaire/:id} : get matriceFacturations by affaireId.
     *
     * @param id the id of the affaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of matriceFacturationDTO.
     */
    @GetMapping("/matrice-facturations/affaire/{id}")
    public ResponseEntity<List<MatriceFacturationDTO>> findMatriceByAffaireId(@PathVariable Long id) {
        log.debug("REST request to get MatriceFacturation by affaire id : {}", id);
        List<MatriceFacturationDTO> result = matriceFacturationService.findMatriceByAffaireId(id);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code DELETE  /matrice-facturations/:id} : delete the "id" matriceFacturation.
     *
     * @param id the id of the matriceFacturationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/matrice-facturations/{id}")
    public ResponseEntity<Void> deleteMatriceFacturation(@PathVariable Long id) {
        log.debug("REST request to delete MatriceFacturation : {}", id);
        matriceFacturationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
