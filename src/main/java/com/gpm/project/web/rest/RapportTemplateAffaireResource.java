package com.gpm.project.web.rest;

import com.gpm.project.domain.RapportTemplateAffaire;
import com.gpm.project.repository.RapportTemplateAffaireRepository;
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
 * REST controller for managing {@link com.gpm.project.domain.RapportTemplateAffaire}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RapportTemplateAffaireResource {

    private final Logger log = LoggerFactory.getLogger(RapportTemplateAffaireResource.class);

    private static final String ENTITY_NAME = "projectServiceRapportTemplateAffaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RapportTemplateAffaireRepository rapportTemplateAffaireRepository;

    public RapportTemplateAffaireResource(RapportTemplateAffaireRepository rapportTemplateAffaireRepository) {
        this.rapportTemplateAffaireRepository = rapportTemplateAffaireRepository;
    }

    /**
     * {@code POST  /rapport-template-affaires} : Create a new rapportTemplateAffaire.
     *
     * @param rapportTemplateAffaire the rapportTemplateAffaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rapportTemplateAffaire, or with status {@code 400 (Bad Request)} if the rapportTemplateAffaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rapport-template-affaires")
    public ResponseEntity<RapportTemplateAffaire> createRapportTemplateAffaire(@RequestBody RapportTemplateAffaire rapportTemplateAffaire)
        throws URISyntaxException {
        log.debug("REST request to save RapportTemplateAffaire : {}", rapportTemplateAffaire);
        if (rapportTemplateAffaire.getId() != null) {
            throw new BadRequestAlertException("A new rapportTemplateAffaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RapportTemplateAffaire result = rapportTemplateAffaireRepository.save(rapportTemplateAffaire);
        return ResponseEntity
            .created(new URI("/api/rapport-template-affaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rapport-template-affaires/:id} : Updates an existing rapportTemplateAffaire.
     *
     * @param id the id of the rapportTemplateAffaire to save.
     * @param rapportTemplateAffaire the rapportTemplateAffaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportTemplateAffaire,
     * or with status {@code 400 (Bad Request)} if the rapportTemplateAffaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rapportTemplateAffaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rapport-template-affaires/{id}")
    public ResponseEntity<RapportTemplateAffaire> updateRapportTemplateAffaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RapportTemplateAffaire rapportTemplateAffaire
    ) throws URISyntaxException {
        log.debug("REST request to update RapportTemplateAffaire : {}, {}", id, rapportTemplateAffaire);
        if (rapportTemplateAffaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapportTemplateAffaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportTemplateAffaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RapportTemplateAffaire result = rapportTemplateAffaireRepository.save(rapportTemplateAffaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportTemplateAffaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rapport-template-affaires/:id} : Partial updates given fields of an existing rapportTemplateAffaire, field will ignore if it is null
     *
     * @param id the id of the rapportTemplateAffaire to save.
     * @param rapportTemplateAffaire the rapportTemplateAffaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rapportTemplateAffaire,
     * or with status {@code 400 (Bad Request)} if the rapportTemplateAffaire is not valid,
     * or with status {@code 404 (Not Found)} if the rapportTemplateAffaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the rapportTemplateAffaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rapport-template-affaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RapportTemplateAffaire> partialUpdateRapportTemplateAffaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RapportTemplateAffaire rapportTemplateAffaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update RapportTemplateAffaire partially : {}, {}", id, rapportTemplateAffaire);
        if (rapportTemplateAffaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rapportTemplateAffaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rapportTemplateAffaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RapportTemplateAffaire> result = rapportTemplateAffaireRepository
            .findById(rapportTemplateAffaire.getId())
            .map(existingRapportTemplateAffaire -> {
                if (rapportTemplateAffaire.getAffaireId() != null) {
                    existingRapportTemplateAffaire.setAffaireId(rapportTemplateAffaire.getAffaireId());
                }
                if (rapportTemplateAffaire.getRapportTemplateId() != null) {
                    existingRapportTemplateAffaire.setRapportTemplateId(rapportTemplateAffaire.getRapportTemplateId());
                }

                return existingRapportTemplateAffaire;
            })
            .map(rapportTemplateAffaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rapportTemplateAffaire.getId().toString())
        );
    }

    /**
     * {@code GET  /rapport-template-affaires} : get all the rapportTemplateAffaires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rapportTemplateAffaires in body.
     */
    @GetMapping("/rapport-template-affaires")
    public List<RapportTemplateAffaire> getAllRapportTemplateAffaires() {
        log.debug("REST request to get all RapportTemplateAffaires");
        return rapportTemplateAffaireRepository.findAll();
    }

    /**
     * {@code GET  /rapport-template-affaires/:id} : get the "id" rapportTemplateAffaire.
     *
     * @param id the id of the rapportTemplateAffaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rapportTemplateAffaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rapport-template-affaires/{id}")
    public ResponseEntity<RapportTemplateAffaire> getRapportTemplateAffaire(@PathVariable Long id) {
        log.debug("REST request to get RapportTemplateAffaire : {}", id);
        Optional<RapportTemplateAffaire> rapportTemplateAffaire = rapportTemplateAffaireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rapportTemplateAffaire);
    }

    /**
     * {@code DELETE  /rapport-template-affaires/:id} : delete the "id" rapportTemplateAffaire.
     *
     * @param id the id of the rapportTemplateAffaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rapport-template-affaires/{id}")
    public ResponseEntity<Void> deleteRapportTemplateAffaire(@PathVariable Long id) {
        log.debug("REST request to delete RapportTemplateAffaire : {}", id);
        rapportTemplateAffaireRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
