package com.gpm.project.web.rest;

import com.gpm.project.repository.RoleContactSocieteRepository;
import com.gpm.project.service.RoleContactSocieteService;
import com.gpm.project.service.dto.RoleContactSocieteDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.RoleContactSociete}.
 */
@RestController
@RequestMapping("/api")
public class RoleContactSocieteResource {

    private final Logger log = LoggerFactory.getLogger(RoleContactSocieteResource.class);

    private static final String ENTITY_NAME = "projectServiceRoleContactSociete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleContactSocieteService roleContactSocieteService;

    private final RoleContactSocieteRepository roleContactSocieteRepository;

    public RoleContactSocieteResource(
        RoleContactSocieteService roleContactSocieteService,
        RoleContactSocieteRepository roleContactSocieteRepository
    ) {
        this.roleContactSocieteService = roleContactSocieteService;
        this.roleContactSocieteRepository = roleContactSocieteRepository;
    }

    /**
     * {@code POST  /role-contact-societes} : Create a new roleContactSociete.
     *
     * @param roleContactSocieteDTO the roleContactSocieteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleContactSocieteDTO, or with status {@code 400 (Bad Request)} if the roleContactSociete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/role-contact-societes")
    public ResponseEntity<RoleContactSocieteDTO> createRoleContactSociete(@RequestBody RoleContactSocieteDTO roleContactSocieteDTO)
        throws URISyntaxException {
        log.debug("REST request to save RoleContactSociete : {}", roleContactSocieteDTO);
        if (roleContactSocieteDTO.getId() != null) {
            throw new BadRequestAlertException("A new roleContactSociete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoleContactSocieteDTO result = roleContactSocieteService.save(roleContactSocieteDTO);
        return ResponseEntity
            .created(new URI("/api/role-contact-societes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /role-contact-societes/:id} : Updates an existing roleContactSociete.
     *
     * @param id the id of the roleContactSocieteDTO to save.
     * @param roleContactSocieteDTO the roleContactSocieteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleContactSocieteDTO,
     * or with status {@code 400 (Bad Request)} if the roleContactSocieteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleContactSocieteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/role-contact-societes/{id}")
    public ResponseEntity<RoleContactSocieteDTO> updateRoleContactSociete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleContactSocieteDTO roleContactSocieteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RoleContactSociete : {}, {}", id, roleContactSocieteDTO);
        if (roleContactSocieteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleContactSocieteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleContactSocieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleContactSocieteDTO result = roleContactSocieteService.update(roleContactSocieteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleContactSocieteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /role-contact-societes/:id} : Partial updates given fields of an existing roleContactSociete, field will ignore if it is null
     *
     * @param id the id of the roleContactSocieteDTO to save.
     * @param roleContactSocieteDTO the roleContactSocieteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleContactSocieteDTO,
     * or with status {@code 400 (Bad Request)} if the roleContactSocieteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the roleContactSocieteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleContactSocieteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/role-contact-societes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleContactSocieteDTO> partialUpdateRoleContactSociete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleContactSocieteDTO roleContactSocieteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoleContactSociete partially : {}, {}", id, roleContactSocieteDTO);
        if (roleContactSocieteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleContactSocieteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleContactSocieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleContactSocieteDTO> result = roleContactSocieteService.partialUpdate(roleContactSocieteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleContactSocieteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /role-contact-societes} : get all the roleContactSocietes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleContactSocietes in body.
     */
    @GetMapping("/role-contact-societes")
    public List<RoleContactSocieteDTO> getAllRoleContactSocietes() {
        log.debug("REST request to get all RoleContactSocietes");
        return roleContactSocieteService.findAll();
    }

    /**
     * {@code GET  /role-contact-societes/:id} : get the "id" roleContactSociete.
     *
     * @param id the id of the roleContactSocieteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleContactSocieteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/role-contact-societes/{id}")
    public ResponseEntity<RoleContactSocieteDTO> getRoleContactSociete(@PathVariable Long id) {
        log.debug("REST request to get RoleContactSociete : {}", id);
        Optional<RoleContactSocieteDTO> roleContactSocieteDTO = roleContactSocieteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleContactSocieteDTO);
    }

    /**
     * {@code DELETE  /role-contact-societes/:id} : delete the "id" roleContactSociete.
     *
     * @param id the id of the roleContactSocieteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/role-contact-societes/{id}")
    public ResponseEntity<Void> deleteRoleContactSociete(@PathVariable Long id) {
        log.debug("REST request to delete RoleContactSociete : {}", id);
        roleContactSocieteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    //    @GetMapping("/role-contact-societes")
    //    public List<RoleContactSocieteDTO> getAll() {
    //        return roleContactSocieteService.findAll();
    //    }
}
