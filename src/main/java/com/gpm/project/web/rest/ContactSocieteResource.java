package com.gpm.project.web.rest;

import com.gpm.project.repository.ContactSocieteRepository;
import com.gpm.project.service.ContactSocieteService;
import com.gpm.project.service.dto.ContactSocieteDTO;
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
 * REST controller for managing {@link com.gpm.project.domain.ContactSociete}.
 */
@RestController
@RequestMapping("/api")
public class ContactSocieteResource {

    private final Logger log = LoggerFactory.getLogger(ContactSocieteResource.class);

    private static final String ENTITY_NAME = "projectServiceContactSociete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactSocieteService contactSocieteService;

    private final ContactSocieteRepository contactSocieteRepository;

    public ContactSocieteResource(ContactSocieteService contactSocieteService, ContactSocieteRepository contactSocieteRepository) {
        this.contactSocieteService = contactSocieteService;
        this.contactSocieteRepository = contactSocieteRepository;
    }

    /**
     * {@code POST  /contact-societes} : Create a new contactSociete.
     *
     * @param contactSocieteDTO the contactSocieteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactSocieteDTO, or with status {@code 400 (Bad Request)} if the contactSociete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-societes")
    public ResponseEntity<ContactSocieteDTO> createContactSociete(@RequestBody ContactSocieteDTO contactSocieteDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactSociete : {}", contactSocieteDTO);
        if (contactSocieteDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactSociete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactSocieteDTO result = contactSocieteService.save(contactSocieteDTO);
        return ResponseEntity
            .created(new URI("/api/contact-societes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-societes/:id} : Updates an existing contactSociete.
     *
     * @param id the id of the contactSocieteDTO to save.
     * @param contactSocieteDTO the contactSocieteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactSocieteDTO,
     * or with status {@code 400 (Bad Request)} if the contactSocieteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactSocieteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-societes/{id}")
    public ResponseEntity<ContactSocieteDTO> updateContactSociete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactSocieteDTO contactSocieteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactSociete : {}, {}", id, contactSocieteDTO);
        if (contactSocieteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactSocieteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactSocieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactSocieteDTO result = contactSocieteService.update(contactSocieteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactSocieteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-societes/:id} : Partial updates given fields of an existing contactSociete, field will ignore if it is null
     *
     * @param id the id of the contactSocieteDTO to save.
     * @param contactSocieteDTO the contactSocieteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactSocieteDTO,
     * or with status {@code 400 (Bad Request)} if the contactSocieteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactSocieteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactSocieteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-societes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactSocieteDTO> partialUpdateContactSociete(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactSocieteDTO contactSocieteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactSociete partially : {}, {}", id, contactSocieteDTO);
        if (contactSocieteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactSocieteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactSocieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactSocieteDTO> result = contactSocieteService.partialUpdate(contactSocieteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactSocieteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-societes} : get all the contactSocietes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactSocietes in body.
     */
    @GetMapping("/contact-societes")
    public ResponseEntity<List<ContactSocieteDTO>> getAllContactSocietes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ContactSocietes");
        Page<ContactSocieteDTO> page = contactSocieteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/contact-societes/by-spciete-id")
    public ResponseEntity<List<ContactSocieteDTO>> getAllContactSocietesBySocieteId(@RequestParam Long societeId) {
        log.debug("REST request to get all ContactSocietes by SocieteId: {}", societeId);
        List<ContactSocieteDTO> contactSocietes = contactSocieteService.findAllBySocieteId(societeId);
        return ResponseEntity.ok().body(contactSocietes);
    }

    /**
     * {@code GET  /contact-societes/:id} : get the "id" contactSociete.
     *
     * @param id the id of the contactSocieteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactSocieteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-societes/{id}")
    public ResponseEntity<ContactSocieteDTO> getContactSociete(@PathVariable Long id) {
        log.debug("REST request to get ContactSociete : {}", id);
        Optional<ContactSocieteDTO> contactSocieteDTO = contactSocieteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactSocieteDTO);
    }

    /**
     * {@code DELETE  /contact-societes/:id} : delete the "id" contactSociete.
     *
     * @param id the id of the contactSocieteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-societes/{id}")
    public ResponseEntity<Void> deleteContactSociete(@PathVariable Long id) {
        log.debug("REST request to delete ContactSociete : {}", id);
        contactSocieteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
