package com.gpm.project.web.rest;

import com.gpm.project.domain.AclEntryProjet;
import com.gpm.project.repository.AclEntryProjetRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gpm.project.domain.AclEntryProjet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AclEntryProjetResource {

    private final Logger log = LoggerFactory.getLogger(AclEntryProjetResource.class);

    private static final String ENTITY_NAME = "projectServiceAclEntryProjet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AclEntryProjetRepository aclEntryProjetRepository;

    public AclEntryProjetResource(AclEntryProjetRepository aclEntryProjetRepository) {
        this.aclEntryProjetRepository = aclEntryProjetRepository;
    }

    /**
     * {@code POST  /acl-entry-projets} : Create a new aclEntryProjet.
     *
     * @param aclEntryProjet the aclEntryProjet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aclEntryProjet, or with status {@code 400 (Bad Request)} if the aclEntryProjet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acl-entry-projets")
    public ResponseEntity<AclEntryProjet> createAclEntryProjet(@RequestBody AclEntryProjet aclEntryProjet) throws URISyntaxException {
        log.debug("REST request to save AclEntryProjet : {}", aclEntryProjet);
        if (aclEntryProjet.getId() != null) {
            throw new BadRequestAlertException("A new aclEntryProjet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AclEntryProjet result = aclEntryProjetRepository.save(aclEntryProjet);
        return ResponseEntity
            .created(new URI("/api/acl-entry-projets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acl-entry-projets/:id} : Updates an existing aclEntryProjet.
     *
     * @param id the id of the aclEntryProjet to save.
     * @param aclEntryProjet the aclEntryProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclEntryProjet,
     * or with status {@code 400 (Bad Request)} if the aclEntryProjet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aclEntryProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acl-entry-projets/{id}")
    public ResponseEntity<AclEntryProjet> updateAclEntryProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclEntryProjet aclEntryProjet
    ) throws URISyntaxException {
        log.debug("REST request to update AclEntryProjet : {}, {}", id, aclEntryProjet);
        if (aclEntryProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclEntryProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclEntryProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AclEntryProjet result = aclEntryProjetRepository.save(aclEntryProjet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclEntryProjet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acl-entry-projets/:id} : Partial updates given fields of an existing aclEntryProjet, field will ignore if it is null
     *
     * @param id the id of the aclEntryProjet to save.
     * @param aclEntryProjet the aclEntryProjet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aclEntryProjet,
     * or with status {@code 400 (Bad Request)} if the aclEntryProjet is not valid,
     * or with status {@code 404 (Not Found)} if the aclEntryProjet is not found,
     * or with status {@code 500 (Internal Server Error)} if the aclEntryProjet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acl-entry-projets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AclEntryProjet> partialUpdateAclEntryProjet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AclEntryProjet aclEntryProjet
    ) throws URISyntaxException {
        log.debug("REST request to partial update AclEntryProjet partially : {}, {}", id, aclEntryProjet);
        if (aclEntryProjet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aclEntryProjet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aclEntryProjetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AclEntryProjet> result = aclEntryProjetRepository
            .findById(aclEntryProjet.getId())
            .map(existingAclEntryProjet -> {
                if (aclEntryProjet.getProject_id() != null) {
                    existingAclEntryProjet.setProject_id(aclEntryProjet.getProject_id());
                }
                if (aclEntryProjet.getSid_id() != null) {
                    existingAclEntryProjet.setSid_id(aclEntryProjet.getSid_id());
                }
                if (aclEntryProjet.getSid() != null) {
                    existingAclEntryProjet.setSid(aclEntryProjet.getSid());
                }
                if (aclEntryProjet.getCan_read() != null) {
                    existingAclEntryProjet.setCan_read(aclEntryProjet.getCan_read());
                }
                if (aclEntryProjet.getCan_write() != null) {
                    existingAclEntryProjet.setCan_write(aclEntryProjet.getCan_write());
                }

                return existingAclEntryProjet;
            })
            .map(aclEntryProjetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aclEntryProjet.getId().toString())
        );
    }

    /**
     * {@code GET  /acl-entry-projets} : get all the aclEntryProjets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aclEntryProjets in body.
     */
    @GetMapping("/acl-entry-projets")
    public List<AclEntryProjet> getAllAclEntryProjets() {
        log.debug("REST request to get all AclEntryProjets");
        return aclEntryProjetRepository.findAll();
    }

    /**
     * {@code GET  /acl-entry-projets/:id} : get the "id" aclEntryProjet.
     *
     * @param id the id of the aclEntryProjet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aclEntryProjet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acl-entry-projets/{id}")
    public ResponseEntity<AclEntryProjet> getAclEntryProjet(@PathVariable Long id) {
        log.debug("REST request to get AclEntryProjet : {}", id);
        Optional<AclEntryProjet> aclEntryProjet = aclEntryProjetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aclEntryProjet);
    }

    /**
     * {@code DELETE  /acl-entry-projets/:id} : delete the "id" aclEntryProjet.
     *
     * @param id the id of the aclEntryProjet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acl-entry-projets/{id}")
    public ResponseEntity<Void> deleteAclEntryProjet(@PathVariable Long id) {
        log.debug("REST request to delete AclEntryProjet : {}", id);
        aclEntryProjetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
