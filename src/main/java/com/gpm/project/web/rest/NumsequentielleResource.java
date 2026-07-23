package com.gpm.project.web.rest;

import com.gpm.project.repository.NumsequentielleRepository;
import com.gpm.project.service.NumsequentielleService;
import com.gpm.project.service.dto.NumsequentielleDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.Numsequentielle}.
 */
@RestController
@RequestMapping("/api")
public class NumsequentielleResource {

    private final Logger log = LoggerFactory.getLogger(NumsequentielleResource.class);

    private static final String ENTITY_NAME = "projectServiceNumsequentielle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NumsequentielleService numsequentielleService;

    private final NumsequentielleRepository numsequentielleRepository;

    public NumsequentielleResource(NumsequentielleService numsequentielleService, NumsequentielleRepository numsequentielleRepository) {
        this.numsequentielleService = numsequentielleService;
        this.numsequentielleRepository = numsequentielleRepository;
    }

    /**
     * {@code POST  /numsequentielles} : Create a new numsequentielle.
     *
     * @param numsequentielleDTO the numsequentielleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new numsequentielleDTO, or with status {@code 400 (Bad Request)} if the numsequentielle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/numsequentielles")
    public ResponseEntity<NumsequentielleDTO> createNumsequentielle(@Valid @RequestBody NumsequentielleDTO numsequentielleDTO)
        throws URISyntaxException {
        log.debug("REST request to save Numsequentielle : {}", numsequentielleDTO);
        if (numsequentielleDTO.getId() != null) {
            throw new BadRequestAlertException("A new numsequentielle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NumsequentielleDTO result = numsequentielleService.save(numsequentielleDTO);
        return ResponseEntity
            .created(new URI("/api/numsequentielles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /numsequentielles/:id} : Updates an existing numsequentielle.
     *
     * @param id the id of the numsequentielleDTO to save.
     * @param numsequentielleDTO the numsequentielleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated numsequentielleDTO,
     * or with status {@code 400 (Bad Request)} if the numsequentielleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the numsequentielleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/numsequentielles/{id}")
    public ResponseEntity<NumsequentielleDTO> updateNumsequentielle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NumsequentielleDTO numsequentielleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Numsequentielle : {}, {}", id, numsequentielleDTO);
        if (numsequentielleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, numsequentielleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!numsequentielleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NumsequentielleDTO result = numsequentielleService.save(numsequentielleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, numsequentielleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /numsequentielles/:id} : Partial updates given fields of an existing numsequentielle, field will ignore if it is null
     *
     * @param id the id of the numsequentielleDTO to save.
     * @param numsequentielleDTO the numsequentielleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated numsequentielleDTO,
     * or with status {@code 400 (Bad Request)} if the numsequentielleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the numsequentielleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the numsequentielleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/numsequentielles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NumsequentielleDTO> partialUpdateNumsequentielle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NumsequentielleDTO numsequentielleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Numsequentielle partially : {}, {}", id, numsequentielleDTO);
        if (numsequentielleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, numsequentielleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!numsequentielleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NumsequentielleDTO> result = numsequentielleService.partialUpdate(numsequentielleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, numsequentielleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /numsequentielles} : get all the numsequentielles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of numsequentielles in body.
     */
    @GetMapping("/numsequentielles")
    public ResponseEntity<List<NumsequentielleDTO>> getAllNumsequentielles(Pageable pageable) {
        log.debug("REST request to get a page of Numsequentielles");
        Page<NumsequentielleDTO> page = numsequentielleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /numsequentielles/:id} : get the "id" numsequentielle.
     *
     * @param id the id of the numsequentielleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the numsequentielleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/numsequentielles/{id}")
    public ResponseEntity<NumsequentielleDTO> getNumsequentielle(@PathVariable Long id) {
        log.debug("REST request to get Numsequentielle : {}", id);
        Optional<NumsequentielleDTO> numsequentielleDTO = numsequentielleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(numsequentielleDTO);
    }

    /**
     * {@code DELETE  /numsequentielles/:id} : delete the "id" numsequentielle.
     *
     * @param id the id of the numsequentielleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/numsequentielles/{id}")
    public ResponseEntity<Void> deleteNumsequentielle(@PathVariable Long id) {
        log.debug("REST request to delete Numsequentielle : {}", id);
        numsequentielleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/numsequentielles/generate-identifiant-client")
    public ResponseEntity<String> generateIdentifiantClient() {
        String identifiant = numsequentielleService.genererIdentifiantClient();
        return ResponseEntity.ok(identifiant);
    }
}
