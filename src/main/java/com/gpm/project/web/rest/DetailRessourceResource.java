package com.gpm.project.web.rest;

import com.gpm.project.repository.DetailRessourceRepository;
import com.gpm.project.service.DetailRessourceService;
import com.gpm.project.service.dto.DetailRessourceDTO;
import com.gpm.project.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gpm.project.domain.DetailRessource}.
 */
@RestController
@RequestMapping("/api")
public class DetailRessourceResource {

    private final Logger log = LoggerFactory.getLogger(DetailRessourceResource.class);

    private static final String ENTITY_NAME = "projectServiceDetailRessource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailRessourceService detailRessourceService;

    private final DetailRessourceRepository detailRessourceRepository;

    public DetailRessourceResource(DetailRessourceService detailRessourceService, DetailRessourceRepository detailRessourceRepository) {
        this.detailRessourceService = detailRessourceService;
        this.detailRessourceRepository = detailRessourceRepository;
    }

    /**
     * {@code POST  /detail-ressources} : Create a new detailRessource.
     *
     * @param detailRessourceDTO the detailRessourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailRessourceDTO, or with status {@code 400 (Bad Request)} if the detailRessource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-ressources")
    public ResponseEntity<DetailRessourceDTO> createDetailRessource(@RequestBody DetailRessourceDTO detailRessourceDTO)
        throws URISyntaxException {
        log.debug("REST request to save DetailRessource : {}", detailRessourceDTO);
        if (detailRessourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new detailRessource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailRessourceDTO result = detailRessourceService.save(detailRessourceDTO);
        return ResponseEntity
            .created(new URI("/api/detail-ressources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-ressources/:id} : Updates an existing detailRessource.
     *
     * @param id the id of the detailRessourceDTO to save.
     * @param detailRessourceDTO the detailRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the detailRessourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-ressources/{id}")
    public ResponseEntity<DetailRessourceDTO> updateDetailRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailRessourceDTO detailRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DetailRessource : {}, {}", id, detailRessourceDTO);
        if (detailRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailRessourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailRessourceDTO result = detailRessourceService.save(detailRessourceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailRessourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detail-ressources/:id} : Partial updates given fields of an existing detailRessource, field will ignore if it is null
     *
     * @param id the id of the detailRessourceDTO to save.
     * @param detailRessourceDTO the detailRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the detailRessourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the detailRessourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-ressources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailRessourceDTO> partialUpdateDetailRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailRessourceDTO detailRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailRessource partially : {}, {}", id, detailRessourceDTO);
        if (detailRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailRessourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailRessourceDTO> result = detailRessourceService.partialUpdate(detailRessourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailRessourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-ressources} : get all the detailRessources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailRessources in body.
     */
    @GetMapping("/detail-ressources")
    public ResponseEntity<List<DetailRessourceDTO>> getAllDetailRessources(Pageable pageable) {
        log.debug("REST request to get a page of DetailRessources");
        Page<DetailRessourceDTO> page = detailRessourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detail-ressources/:id} : get the "id" detailRessource.
     *
     * @param id the id of the detailRessourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailRessourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-ressources/{id}")
    public ResponseEntity<DetailRessourceDTO> getDetailRessource(@PathVariable Long id) {
        log.debug("REST request to get DetailRessource : {}", id);
        Optional<DetailRessourceDTO> detailRessourceDTO = detailRessourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailRessourceDTO);
    }

    /**
     * {@code DELETE  /detail-ressources/:id} : delete the "id" detailRessource.
     *
     * @param id the id of the detailRessourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-ressources/{id}")
    public ResponseEntity<Void> deleteDetailRessource(@PathVariable Long id) {
        log.debug("REST request to delete DetailRessource : {}", id);
        detailRessourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
