package com.gpm.project.web.rest;

import com.gpm.project.repository.MatriceJourFerieRepository;
import com.gpm.project.service.MatriceJourFerieService;
import com.gpm.project.service.dto.MatriceJourFerieDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.MatriceJourFerie}.
 */
@RestController
@RequestMapping("/api")
public class MatriceJourFerieResource {

    private final Logger log = LoggerFactory.getLogger(MatriceJourFerieResource.class);

    private static final String ENTITY_NAME = "projectServiceMatriceJourFerie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatriceJourFerieService matriceJourFerieService;

    private final MatriceJourFerieRepository matriceJourFerieRepository;

    public MatriceJourFerieResource(
        MatriceJourFerieService matriceJourFerieService,
        MatriceJourFerieRepository matriceJourFerieRepository
    ) {
        this.matriceJourFerieService = matriceJourFerieService;
        this.matriceJourFerieRepository = matriceJourFerieRepository;
    }

    /**
     * {@code POST  /matrice-jour-feries} : Create a new matriceJourFerie.
     *
     * @param matriceJourFerieDTO the matriceJourFerieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matriceJourFerieDTO, or with status {@code 400 (Bad Request)} if the matriceJourFerie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/matrice-jour-feries")
    public ResponseEntity<MatriceJourFerieDTO> createMatriceJourFerie(@Valid @RequestBody MatriceJourFerieDTO matriceJourFerieDTO)
        throws URISyntaxException {
        log.debug("REST request to save MatriceJourFerie : {}", matriceJourFerieDTO);
        if (matriceJourFerieDTO.getId() != null) {
            throw new BadRequestAlertException("A new matriceJourFerie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MatriceJourFerieDTO result = matriceJourFerieService.save(matriceJourFerieDTO);
        return ResponseEntity
            .created(new URI("/api/matrice-jour-feries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /matrice-jour-feries/:id} : Updates an existing matriceJourFerie.
     *
     * @param id the id of the matriceJourFerieDTO to save.
     * @param matriceJourFerieDTO the matriceJourFerieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matriceJourFerieDTO,
     * or with status {@code 400 (Bad Request)} if the matriceJourFerieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matriceJourFerieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/matrice-jour-feries/{id}")
    public ResponseEntity<MatriceJourFerieDTO> updateMatriceJourFerie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatriceJourFerieDTO matriceJourFerieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MatriceJourFerie : {}, {}", id, matriceJourFerieDTO);
        if (matriceJourFerieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matriceJourFerieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriceJourFerieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MatriceJourFerieDTO result = matriceJourFerieService.update(matriceJourFerieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matriceJourFerieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /matrice-jour-feries/:id} : Partial updates given fields of an existing matriceJourFerie, field will ignore if it is null
     *
     * @param id the id of the matriceJourFerieDTO to save.
     * @param matriceJourFerieDTO the matriceJourFerieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matriceJourFerieDTO,
     * or with status {@code 400 (Bad Request)} if the matriceJourFerieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matriceJourFerieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matriceJourFerieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/matrice-jour-feries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatriceJourFerieDTO> partialUpdateMatriceJourFerie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatriceJourFerieDTO matriceJourFerieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MatriceJourFerie partially : {}, {}", id, matriceJourFerieDTO);
        if (matriceJourFerieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matriceJourFerieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matriceJourFerieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatriceJourFerieDTO> result = matriceJourFerieService.partialUpdate(matriceJourFerieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matriceJourFerieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /matrice-jour-feries} : get all the matriceJourFeries.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matriceJourFeries in body.
     */
    @GetMapping("/matrice-jour-feries")
    public ResponseEntity<List<MatriceJourFerieDTO>> getAllMatriceJourFeries(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of MatriceJourFeries");
        Page<MatriceJourFerieDTO> page;
        if (eagerload) {
            page = matriceJourFerieService.findAllWithEagerRelationships(pageable);
        } else {
            page = matriceJourFerieService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /matrice-jour-feries/:id} : get the "id" matriceJourFerie.
     *
     * @param id the id of the matriceJourFerieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matriceJourFerieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/matrice-jour-feries/{id}")
    public ResponseEntity<MatriceJourFerieDTO> getMatriceJourFerie(@PathVariable Long id) {
        log.debug("REST request to get MatriceJourFerie : {}", id);
        Optional<MatriceJourFerieDTO> matriceJourFerieDTO = matriceJourFerieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matriceJourFerieDTO);
    }

    /**
     * {@code DELETE  /matrice-jour-feries/:id} : delete the "id" matriceJourFerie.
     *
     * @param id the id of the matriceJourFerieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/matrice-jour-feries/{id}")
    public ResponseEntity<Void> deleteMatriceJourFerie(@PathVariable Long id) {
        log.debug("REST request to delete MatriceJourFerie : {}", id);
        matriceJourFerieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
