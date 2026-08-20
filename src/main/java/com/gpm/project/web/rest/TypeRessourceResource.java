package com.gpm.project.web.rest;

import com.gpm.project.repository.TypeRessourceRepository;
import com.gpm.project.service.TypeRessourceService;
import com.gpm.project.service.dto.TypeRessourceDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.TypeRessource}.
 */
@RestController
@RequestMapping("/api")
public class TypeRessourceResource {

    private final Logger log = LoggerFactory.getLogger(TypeRessourceResource.class);

    private static final String ENTITY_NAME = "projectServiceTypeRessource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeRessourceService typeRessourceService;

    private final TypeRessourceRepository typeRessourceRepository;

    public TypeRessourceResource(TypeRessourceService typeRessourceService, TypeRessourceRepository typeRessourceRepository) {
        this.typeRessourceService = typeRessourceService;
        this.typeRessourceRepository = typeRessourceRepository;
    }

    /**
     * {@code POST  /type-ressources} : Create a new typeRessource.
     *
     * @param typeRessourceDTO the typeRessourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeRessourceDTO, or with status {@code 400 (Bad Request)} if the typeRessource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-ressources")
    public ResponseEntity<TypeRessourceDTO> createTypeRessource(@RequestBody TypeRessourceDTO typeRessourceDTO) throws URISyntaxException {
        log.debug("REST request to save TypeRessource : {}", typeRessourceDTO);
        if (typeRessourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeRessource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeRessourceDTO result = typeRessourceService.save(typeRessourceDTO);
        return ResponseEntity
            .created(new URI("/api/type-ressources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-ressources/:id} : Updates an existing typeRessource.
     *
     * @param id the id of the typeRessourceDTO to save.
     * @param typeRessourceDTO the typeRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the typeRessourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-ressources/{id}")
    public ResponseEntity<TypeRessourceDTO> updateTypeRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRessourceDTO typeRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeRessource : {}, {}", id, typeRessourceDTO);
        if (typeRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRessourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeRessourceDTO result = typeRessourceService.save(typeRessourceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeRessourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-ressources/:id} : Partial updates given fields of an existing typeRessource, field will ignore if it is null
     *
     * @param id the id of the typeRessourceDTO to save.
     * @param typeRessourceDTO the typeRessourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRessourceDTO,
     * or with status {@code 400 (Bad Request)} if the typeRessourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeRessourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeRessourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-ressources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeRessourceDTO> partialUpdateTypeRessource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRessourceDTO typeRessourceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeRessource partially : {}, {}", id, typeRessourceDTO);
        if (typeRessourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRessourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRessourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeRessourceDTO> result = typeRessourceService.partialUpdate(typeRessourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeRessourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-ressources} : get all the typeRessources.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeRessources in body.
     */
    @GetMapping("/type-ressources")
    public ResponseEntity<List<TypeRessourceDTO>> getAllTypeRessources(Pageable pageable) {
        log.debug("REST request to get a page of TypeRessources");
        Page<TypeRessourceDTO> page = typeRessourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-ressources/list} : get all the typeRessources as a simple list (id, type, code),
     * without pagination. Useful for dropdowns/select inputs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the full list of typeRessources in body.
     */
    @GetMapping("/type-ressources/list")
    public ResponseEntity<List<TypeRessourceDTO>> getAllTypeRessourcesList() {
        log.debug("REST request to get all TypeRessources as list");
        List<TypeRessourceDTO> list = typeRessourceService.findAllList();
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /type-ressources/:id} : get the "id" typeRessource.
     *
     * @param id the id of the typeRessourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeRessourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-ressources/{id}")
    public ResponseEntity<TypeRessourceDTO> getTypeRessource(@PathVariable Long id) {
        log.debug("REST request to get TypeRessource : {}", id);
        Optional<TypeRessourceDTO> typeRessourceDTO = typeRessourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeRessourceDTO);
    }

    /**
     * {@code DELETE  /type-ressources/:id} : delete the "id" typeRessource.
     *
     * @param id the id of the typeRessourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-ressources/{id}")
    public ResponseEntity<Void> deleteTypeRessource(@PathVariable Long id) {
        log.debug("REST request to delete TypeRessource : {}", id);
        typeRessourceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
