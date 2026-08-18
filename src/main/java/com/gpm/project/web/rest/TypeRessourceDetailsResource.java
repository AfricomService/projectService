package com.gpm.project.web.rest;

import com.gpm.project.repository.TypeRessourceDetailsRepository;
import com.gpm.project.service.TypeRessourceDetailsService;
import com.gpm.project.service.dto.TypeRessourceDetailsDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.TypeRessourceDetails}.
 */
@RestController
@RequestMapping("/api")
public class TypeRessourceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TypeRessourceDetailsResource.class);

    private static final String ENTITY_NAME = "projectServiceTypeRessourceDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeRessourceDetailsService typeRessourceDetailsService;

    private final TypeRessourceDetailsRepository typeRessourceDetailsRepository;

    public TypeRessourceDetailsResource(
        TypeRessourceDetailsService typeRessourceDetailsService,
        TypeRessourceDetailsRepository typeRessourceDetailsRepository
    ) {
        this.typeRessourceDetailsService = typeRessourceDetailsService;
        this.typeRessourceDetailsRepository = typeRessourceDetailsRepository;
    }

    /**
     * {@code POST  /type-ressource-details} : Create a new typeRessourceDetails.
     *
     * @param typeRessourceDetailsDTO the typeRessourceDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeRessourceDetailsDTO, or with status {@code 400 (Bad Request)} if the typeRessourceDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-ressource-details")
    public ResponseEntity<TypeRessourceDetailsDTO> createTypeRessourceDetails(@RequestBody TypeRessourceDetailsDTO typeRessourceDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save TypeRessourceDetails : {}", typeRessourceDetailsDTO);
        if (typeRessourceDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeRessourceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeRessourceDetailsDTO result = typeRessourceDetailsService.save(typeRessourceDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/type-ressource-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-ressource-details/:id} : Updates an existing typeRessourceDetails.
     *
     * @param id the id of the typeRessourceDetailsDTO to save.
     * @param typeRessourceDetailsDTO the typeRessourceDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRessourceDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the typeRessourceDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeRessourceDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-ressource-details/{id}")
    public ResponseEntity<TypeRessourceDetailsDTO> updateTypeRessourceDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRessourceDetailsDTO typeRessourceDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeRessourceDetails : {}, {}", id, typeRessourceDetailsDTO);
        if (typeRessourceDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRessourceDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRessourceDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeRessourceDetailsDTO result = typeRessourceDetailsService.save(typeRessourceDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeRessourceDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-ressource-details/:id} : Partial updates given fields of an existing typeRessourceDetails, field will ignore if it is null
     *
     * @param id the id of the typeRessourceDetailsDTO to save.
     * @param typeRessourceDetailsDTO the typeRessourceDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRessourceDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the typeRessourceDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeRessourceDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeRessourceDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-ressource-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeRessourceDetailsDTO> partialUpdateTypeRessourceDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRessourceDetailsDTO typeRessourceDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeRessourceDetails partially : {}, {}", id, typeRessourceDetailsDTO);
        if (typeRessourceDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRessourceDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRessourceDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeRessourceDetailsDTO> result = typeRessourceDetailsService.partialUpdate(typeRessourceDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeRessourceDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-ressource-details} : get all the typeRessourceDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeRessourceDetails in body.
     */
    @GetMapping("/type-ressource-details")
    public List<TypeRessourceDetailsDTO> getAllTypeRessourceDetails() {
        log.debug("REST request to get all TypeRessourceDetails");
        return typeRessourceDetailsService.findAll();
    }

    /**
     * {@code GET  /type-ressource-details/:id} : get the "id" typeRessourceDetails.
     *
     * @param id the id of the typeRessourceDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeRessourceDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-ressource-details/{id}")
    public ResponseEntity<TypeRessourceDetailsDTO> getTypeRessourceDetails(@PathVariable Long id) {
        log.debug("REST request to get TypeRessourceDetails : {}", id);
        Optional<TypeRessourceDetailsDTO> typeRessourceDetailsDTO = typeRessourceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeRessourceDetailsDTO);
    }

    /**
     * {@code DELETE  /type-ressource-details/:id} : delete the "id" typeRessourceDetails.
     *
     * @param id the id of the typeRessourceDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-ressource-details/{id}")
    public ResponseEntity<Void> deleteTypeRessourceDetails(@PathVariable Long id) {
        log.debug("REST request to delete TypeRessourceDetails : {}", id);
        typeRessourceDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
