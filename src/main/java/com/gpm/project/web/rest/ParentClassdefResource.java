package com.gpm.project.web.rest;

import com.gpm.project.repository.ParentClassdefRepository;
import com.gpm.project.service.ParentClassdefService;
import com.gpm.project.service.dto.ParentClassdefDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.ParentClassdef}.
 */
@RestController
@RequestMapping("/api")
public class ParentClassdefResource {

    private final Logger log = LoggerFactory.getLogger(ParentClassdefResource.class);

    private static final String ENTITY_NAME = "projectServiceParentClassdef";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParentClassdefService parentClassdefService;

    private final ParentClassdefRepository parentClassdefRepository;

    public ParentClassdefResource(ParentClassdefService parentClassdefService, ParentClassdefRepository parentClassdefRepository) {
        this.parentClassdefService = parentClassdefService;
        this.parentClassdefRepository = parentClassdefRepository;
    }

    /**
     * {@code POST  /parent-classdefs} : Create a new parentClassdef.
     *
     * @param parentClassdefDTO the parentClassdefDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parentClassdefDTO, or with status {@code 400 (Bad Request)} if the parentClassdef has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parent-classdefs")
    public ResponseEntity<ParentClassdefDTO> createParentClassdef(@RequestBody ParentClassdefDTO parentClassdefDTO)
        throws URISyntaxException {
        log.debug("REST request to save ParentClassdef : {}", parentClassdefDTO);
        if (parentClassdefDTO.getId() != null) {
            throw new BadRequestAlertException("A new parentClassdef cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParentClassdefDTO result = parentClassdefService.save(parentClassdefDTO);
        return ResponseEntity
            .created(new URI("/api/parent-classdefs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parent-classdefs/:id} : Updates an existing parentClassdef.
     *
     * @param id the id of the parentClassdefDTO to save.
     * @param parentClassdefDTO the parentClassdefDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parentClassdefDTO,
     * or with status {@code 400 (Bad Request)} if the parentClassdefDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parentClassdefDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parent-classdefs/{id}")
    public ResponseEntity<ParentClassdefDTO> updateParentClassdef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParentClassdefDTO parentClassdefDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParentClassdef : {}, {}", id, parentClassdefDTO);
        if (parentClassdefDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parentClassdefDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parentClassdefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParentClassdefDTO result = parentClassdefService.save(parentClassdefDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parentClassdefDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parent-classdefs/:id} : Partial updates given fields of an existing parentClassdef, field will ignore if it is null
     *
     * @param id the id of the parentClassdefDTO to save.
     * @param parentClassdefDTO the parentClassdefDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parentClassdefDTO,
     * or with status {@code 400 (Bad Request)} if the parentClassdefDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parentClassdefDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parentClassdefDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parent-classdefs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParentClassdefDTO> partialUpdateParentClassdef(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ParentClassdefDTO parentClassdefDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParentClassdef partially : {}, {}", id, parentClassdefDTO);
        if (parentClassdefDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parentClassdefDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parentClassdefRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParentClassdefDTO> result = parentClassdefService.partialUpdate(parentClassdefDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parentClassdefDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parent-classdefs} : get all the parentClassdefs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parentClassdefs in body.
     */
    @GetMapping("/parent-classdefs")
    public ResponseEntity<List<ParentClassdefDTO>> getAllParentClassdefs(Pageable pageable) {
        log.debug("REST request to get a page of ParentClassdefs");
        Page<ParentClassdefDTO> page = parentClassdefService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parent-classdefs/:id} : get the "id" parentClassdef.
     *
     * @param id the id of the parentClassdefDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parentClassdefDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parent-classdefs/{id}")
    public ResponseEntity<ParentClassdefDTO> getParentClassdef(@PathVariable Long id) {
        log.debug("REST request to get ParentClassdef : {}", id);
        Optional<ParentClassdefDTO> parentClassdefDTO = parentClassdefService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parentClassdefDTO);
    }

    /**
     * {@code DELETE  /parent-classdefs/:id} : delete the "id" parentClassdef.
     *
     * @param id the id of the parentClassdefDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parent-classdefs/{id}")
    public ResponseEntity<Void> deleteParentClassdef(@PathVariable Long id) {
        log.debug("REST request to delete ParentClassdef : {}", id);
        parentClassdefService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
