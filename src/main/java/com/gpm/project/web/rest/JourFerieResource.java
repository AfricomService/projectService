package com.gpm.project.web.rest;

import com.gpm.project.repository.JourFerieRepository;
import com.gpm.project.service.JourFerieService;
import com.gpm.project.service.dto.JourFerieDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.JourFerie}.
 */
@RestController
@RequestMapping("/api")
public class JourFerieResource {

    private final Logger log = LoggerFactory.getLogger(JourFerieResource.class);

    private static final String ENTITY_NAME = "projectServiceJourFerie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JourFerieService jourFerieService;

    private final JourFerieRepository jourFerieRepository;

    public JourFerieResource(JourFerieService jourFerieService, JourFerieRepository jourFerieRepository) {
        this.jourFerieService = jourFerieService;
        this.jourFerieRepository = jourFerieRepository;
    }

    /**
     * {@code POST  /jour-feries} : Create a new jourFerie.
     *
     * @param jourFerieDTO the jourFerieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jourFerieDTO, or with status {@code 400 (Bad Request)} if the jourFerie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jour-feries")
    public ResponseEntity<JourFerieDTO> createJourFerie(@Valid @RequestBody JourFerieDTO jourFerieDTO) throws URISyntaxException {
        log.debug("REST request to save JourFerie : {}", jourFerieDTO);
        if (jourFerieDTO.getId() != null) {
            throw new BadRequestAlertException("A new jourFerie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JourFerieDTO result = jourFerieService.save(jourFerieDTO);
        return ResponseEntity
            .created(new URI("/api/jour-feries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jour-feries/:id} : Updates an existing jourFerie.
     *
     * @param id the id of the jourFerieDTO to save.
     * @param jourFerieDTO the jourFerieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jourFerieDTO,
     * or with status {@code 400 (Bad Request)} if the jourFerieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jourFerieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jour-feries/{id}")
    public ResponseEntity<JourFerieDTO> updateJourFerie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JourFerieDTO jourFerieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JourFerie : {}, {}", id, jourFerieDTO);
        if (jourFerieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jourFerieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jourFerieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JourFerieDTO result = jourFerieService.update(jourFerieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jourFerieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /jour-feries/:id} : Partial updates given fields of an existing jourFerie, field will ignore if it is null
     *
     * @param id the id of the jourFerieDTO to save.
     * @param jourFerieDTO the jourFerieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jourFerieDTO,
     * or with status {@code 400 (Bad Request)} if the jourFerieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jourFerieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jourFerieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/jour-feries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JourFerieDTO> partialUpdateJourFerie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JourFerieDTO jourFerieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JourFerie partially : {}, {}", id, jourFerieDTO);
        if (jourFerieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jourFerieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jourFerieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JourFerieDTO> result = jourFerieService.partialUpdate(jourFerieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jourFerieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /jour-feries} : get all the jourFeries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jourFeries in body.
     */
    @GetMapping("/jour-feries")
    public ResponseEntity<List<JourFerieDTO>> getAllJourFeries(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of JourFeries");
        Page<JourFerieDTO> page = jourFerieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jour-feries/:id} : get the "id" jourFerie.
     *
     * @param id the id of the jourFerieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jourFerieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jour-feries/{id}")
    public ResponseEntity<JourFerieDTO> getJourFerie(@PathVariable Long id) {
        log.debug("REST request to get JourFerie : {}", id);
        Optional<JourFerieDTO> jourFerieDTO = jourFerieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jourFerieDTO);
    }

    /**
     * {@code DELETE  /jour-feries/:id} : delete the "id" jourFerie.
     *
     * @param id the id of the jourFerieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jour-feries/{id}")
    public ResponseEntity<Void> deleteJourFerie(@PathVariable Long id) {
        log.debug("REST request to delete JourFerie : {}", id);
        jourFerieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
