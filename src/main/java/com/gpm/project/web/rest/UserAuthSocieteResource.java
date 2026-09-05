package com.gpm.project.web.rest;

import com.gpm.project.repository.UserAuthSocieteRepository;
import com.gpm.project.service.UserAuthSocieteService;
import com.gpm.project.service.dto.AssignRoleDTO;
import com.gpm.project.service.dto.UserAuthSocieteDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.UserAuthSociete}.
 */
@RestController
@RequestMapping("/api")
public class UserAuthSocieteResource {

    private final Logger log = LoggerFactory.getLogger(UserAuthSocieteResource.class);

    private static final String ENTITY_NAME = "projectServiceUserAuthSociete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAuthSocieteService userAuthSocieteService;

    private final UserAuthSocieteRepository userAuthSocieteRepository;

    public UserAuthSocieteResource(UserAuthSocieteService userAuthSocieteService, UserAuthSocieteRepository userAuthSocieteRepository) {
        this.userAuthSocieteService = userAuthSocieteService;
        this.userAuthSocieteRepository = userAuthSocieteRepository;
    }

    /**
     * {@code POST  /user-auth-societes} : Create a new userAuthSociete.
     *
     * @param userAuthSocieteDTO the userAuthSocieteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAuthSocieteDTO, or with status {@code 400 (Bad Request)} if the userAuthSociete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-auth-societes")
    public ResponseEntity<UserAuthSocieteDTO> createUserAuthSociete(@RequestBody UserAuthSocieteDTO userAuthSocieteDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserAuthSociete : {}", userAuthSocieteDTO);
        if (userAuthSocieteDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAuthSociete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAuthSocieteDTO result = userAuthSocieteService.save(userAuthSocieteDTO);
        return ResponseEntity
            .created(new URI("/api/user-auth-societes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-auth-societes/:id} : Updates an existing userAuthSociete.
     *
     * @param id the id of the userAuthSocieteDTO to save.
     * @param userAuthSocieteDTO the userAuthSocieteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuthSocieteDTO,
     * or with status {@code 400 (Bad Request)} if the userAuthSocieteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAuthSocieteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-auth-societes/{id}")
    public ResponseEntity<UserAuthSocieteDTO> updateUserAuthSociete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuthSocieteDTO userAuthSocieteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAuthSociete : {}, {}", id, userAuthSocieteDTO);
        if (userAuthSocieteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuthSocieteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAuthSocieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAuthSocieteDTO result = userAuthSocieteService.update(userAuthSocieteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuthSocieteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-auth-societes/:id} : Partial updates given fields of an existing userAuthSociete, field will ignore if it is null
     *
     * @param id the id of the userAuthSocieteDTO to save.
     * @param userAuthSocieteDTO the userAuthSocieteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAuthSocieteDTO,
     * or with status {@code 400 (Bad Request)} if the userAuthSocieteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAuthSocieteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAuthSocieteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-auth-societes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAuthSocieteDTO> partialUpdateUserAuthSociete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserAuthSocieteDTO userAuthSocieteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAuthSociete partially : {}, {}", id, userAuthSocieteDTO);
        if (userAuthSocieteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAuthSocieteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAuthSocieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAuthSocieteDTO> result = userAuthSocieteService.partialUpdate(userAuthSocieteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAuthSocieteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-auth-societes} : get all the userAuthSocietes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAuthSocietes in body.
     */
    @GetMapping("/user-auth-societes")
    public List<UserAuthSocieteDTO> getAllUserAuthSocietes() {
        log.debug("REST request to get all UserAuthSocietes");
        return userAuthSocieteService.findAll();
    }

    /**
     * {@code GET  /user-auth-societes/:id} : get the "id" userAuthSociete.
     *
     * @param id the id of the userAuthSocieteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAuthSocieteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-auth-societes/{id}")
    public ResponseEntity<UserAuthSocieteDTO> getUserAuthSociete(@PathVariable Long id) {
        log.debug("REST request to get UserAuthSociete : {}", id);
        Optional<UserAuthSocieteDTO> userAuthSocieteDTO = userAuthSocieteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAuthSocieteDTO);
    }

    /**
     * {@code DELETE  /user-auth-societes/:id} : delete the "id" userAuthSociete.
     *
     * @param id the id of the userAuthSocieteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-auth-societes/{id}")
    public ResponseEntity<Void> deleteUserAuthSociete(@PathVariable Long id) {
        log.debug("REST request to delete UserAuthSociete : {}", id);
        userAuthSocieteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST /user-auth-societes/assign-role} : Assign or update a role for a contact in a société.
     *
     * @param dto the assignment information.
     * @return the updated UserAuthSocieteDTO.
     */
    @PostMapping("/user-auth-societes/assign-role")
    public ResponseEntity<UserAuthSocieteDTO> assignRole(@RequestBody AssignRoleDTO dto) {
        log.debug("REST request to assign role : {}", dto);

        UserAuthSocieteDTO result = userAuthSocieteService.assignRole(dto);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/user-auth-societes/unassign-role")
    public ResponseEntity<Void> unassignRole(
        @RequestParam Long societeId,
        @RequestParam Long contactSocieteId,
        @RequestParam Long roleContactSocieteId
    ) {
        log.debug(
            "REST request to unassign role : societeId={}, contactSocieteId={}, roleContactSocieteId={}",
            societeId,
            contactSocieteId,
            roleContactSocieteId
        );
        userAuthSocieteService.unassignRole(societeId, contactSocieteId, roleContactSocieteId);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET /user-auth-societes/by-societe/{societeId}} : get user auth societes by société.
     *
     * @param societeId the société id.
     * @return the list of UserAuthSocieteDTO.
     */
    @GetMapping("/user-auth-societes/by-societe/{societeId}")
    public List<UserAuthSocieteDTO> getUserAuthSocietesBySociete(@PathVariable Long societeId) {
        log.debug("REST request to get UserAuthSocietes by societeId : {}", societeId);

        return userAuthSocieteService.findBySocieteId(societeId);
    }

    /**
     * {@code GET /user-auth-societes/by-role/{roleCode}} : get user auth societes by role code (ex: MANAGER).
     *
     * @param roleCode the role code.
     * @return the list of UserAuthSocieteDTO having that role.
     */
    @GetMapping("/user-auth-societes/by-role/{roleCode}")
    public List<UserAuthSocieteDTO> getUserAuthSocietesByRole(@PathVariable String roleCode) {
        log.debug("REST request to get UserAuthSocietes by role code : {}", roleCode);

        return userAuthSocieteService.findByRoleCode(roleCode);
    }
}
