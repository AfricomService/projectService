package com.gpm.project.web.rest;

import com.gpm.project.repository.VehiculeRepository;
import com.gpm.project.service.VehiculeService;
import com.gpm.project.service.dto.VehiculeDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.Vehicule}.
 */
@RestController
@RequestMapping("/api")
public class VehiculeResource {

    private final Logger log = LoggerFactory.getLogger(VehiculeResource.class);

    private static final String ENTITY_NAME = "projectServiceVehicule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehiculeService vehiculeService;

    private final VehiculeRepository vehiculeRepository;

    public VehiculeResource(VehiculeService vehiculeService, VehiculeRepository vehiculeRepository) {
        this.vehiculeService = vehiculeService;
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * {@code POST  /vehicules} : Create a new vehicule.
     *
     * @param vehiculeDTO the vehiculeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehiculeDTO, or with status {@code 400 (Bad Request)} if the vehicule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicules")
    public ResponseEntity<VehiculeDTO> createVehicule(@Valid @RequestBody VehiculeDTO vehiculeDTO) throws URISyntaxException {
        log.debug("REST request to save Vehicule : {}", vehiculeDTO);
        if (vehiculeDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehiculeDTO result = vehiculeService.save(vehiculeDTO);
        return ResponseEntity
            .created(new URI("/api/vehicules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicules/:id} : Updates an existing vehicule.
     *
     * @param id the id of the vehiculeDTO to save.
     * @param vehiculeDTO the vehiculeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehiculeDTO,
     * or with status {@code 400 (Bad Request)} if the vehiculeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehiculeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicules/{id}")
    public ResponseEntity<VehiculeDTO> updateVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehiculeDTO vehiculeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vehicule : {}, {}", id, vehiculeDTO);
        if (vehiculeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehiculeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VehiculeDTO result = vehiculeService.update(vehiculeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehiculeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehicules/:id} : Partial updates given fields of an existing vehicule, field will ignore if it is null
     *
     * @param id the id of the vehiculeDTO to save.
     * @param vehiculeDTO the vehiculeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehiculeDTO,
     * or with status {@code 400 (Bad Request)} if the vehiculeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehiculeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehiculeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehicules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehiculeDTO> partialUpdateVehicule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehiculeDTO vehiculeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vehicule partially : {}, {}", id, vehiculeDTO);
        if (vehiculeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehiculeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehiculeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehiculeDTO> result = vehiculeService.partialUpdate(vehiculeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehiculeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicules} : get all the vehicules.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicules in body.
     */
    @GetMapping("/vehicules")
    public ResponseEntity<List<VehiculeDTO>> getAllVehicules(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Vehicules");
        Page<VehiculeDTO> page;
        if (eagerload) {
            page = vehiculeService.findAllWithEagerRelationships(pageable);
        } else {
            page = vehiculeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicules/:id} : get the "id" vehicule.
     *
     * @param id the id of the vehiculeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehiculeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicules/{id}")
    public ResponseEntity<VehiculeDTO> getVehicule(@PathVariable Long id) {
        log.debug("REST request to get Vehicule : {}", id);
        Optional<VehiculeDTO> vehiculeDTO = vehiculeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehiculeDTO);
    }

    /**
     * {@code DELETE  /vehicules/:id} : delete the "id" vehicule.
     *
     * @param id the id of the vehiculeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicules/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        log.debug("REST request to delete Vehicule : {}", id);
        vehiculeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
