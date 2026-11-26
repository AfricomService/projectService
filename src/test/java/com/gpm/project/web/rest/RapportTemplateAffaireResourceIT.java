package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.RapportTemplateAffaire;
import com.gpm.project.repository.RapportTemplateAffaireRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RapportTemplateAffaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapportTemplateAffaireResourceIT {

    private static final Long DEFAULT_AFFAIRE_ID = 1L;
    private static final Long UPDATED_AFFAIRE_ID = 2L;

    private static final Long DEFAULT_RAPPORT_TEMPLATE_ID = 1L;
    private static final Long UPDATED_RAPPORT_TEMPLATE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/rapport-template-affaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RapportTemplateAffaireRepository rapportTemplateAffaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportTemplateAffaireMockMvc;

    private RapportTemplateAffaire rapportTemplateAffaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportTemplateAffaire createEntity(EntityManager em) {
        RapportTemplateAffaire rapportTemplateAffaire = new RapportTemplateAffaire()
            .affaireId(DEFAULT_AFFAIRE_ID)
            .rapportTemplateId(DEFAULT_RAPPORT_TEMPLATE_ID);
        return rapportTemplateAffaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RapportTemplateAffaire createUpdatedEntity(EntityManager em) {
        RapportTemplateAffaire rapportTemplateAffaire = new RapportTemplateAffaire()
            .affaireId(UPDATED_AFFAIRE_ID)
            .rapportTemplateId(UPDATED_RAPPORT_TEMPLATE_ID);
        return rapportTemplateAffaire;
    }

    @BeforeEach
    public void initTest() {
        rapportTemplateAffaire = createEntity(em);
    }

    @Test
    @Transactional
    void createRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeCreate = rapportTemplateAffaireRepository.findAll().size();
        // Create the RapportTemplateAffaire
        restRapportTemplateAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isCreated());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeCreate + 1);
        RapportTemplateAffaire testRapportTemplateAffaire = rapportTemplateAffaireList.get(rapportTemplateAffaireList.size() - 1);
        assertThat(testRapportTemplateAffaire.getAffaireId()).isEqualTo(DEFAULT_AFFAIRE_ID);
        assertThat(testRapportTemplateAffaire.getRapportTemplateId()).isEqualTo(DEFAULT_RAPPORT_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void createRapportTemplateAffaireWithExistingId() throws Exception {
        // Create the RapportTemplateAffaire with an existing ID
        rapportTemplateAffaire.setId(1L);

        int databaseSizeBeforeCreate = rapportTemplateAffaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportTemplateAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRapportTemplateAffaires() throws Exception {
        // Initialize the database
        rapportTemplateAffaireRepository.saveAndFlush(rapportTemplateAffaire);

        // Get all the rapportTemplateAffaireList
        restRapportTemplateAffaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapportTemplateAffaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].affaireId").value(hasItem(DEFAULT_AFFAIRE_ID.intValue())))
            .andExpect(jsonPath("$.[*].rapportTemplateId").value(hasItem(DEFAULT_RAPPORT_TEMPLATE_ID.intValue())));
    }

    @Test
    @Transactional
    void getRapportTemplateAffaire() throws Exception {
        // Initialize the database
        rapportTemplateAffaireRepository.saveAndFlush(rapportTemplateAffaire);

        // Get the rapportTemplateAffaire
        restRapportTemplateAffaireMockMvc
            .perform(get(ENTITY_API_URL_ID, rapportTemplateAffaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapportTemplateAffaire.getId().intValue()))
            .andExpect(jsonPath("$.affaireId").value(DEFAULT_AFFAIRE_ID.intValue()))
            .andExpect(jsonPath("$.rapportTemplateId").value(DEFAULT_RAPPORT_TEMPLATE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRapportTemplateAffaire() throws Exception {
        // Get the rapportTemplateAffaire
        restRapportTemplateAffaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRapportTemplateAffaire() throws Exception {
        // Initialize the database
        rapportTemplateAffaireRepository.saveAndFlush(rapportTemplateAffaire);

        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();

        // Update the rapportTemplateAffaire
        RapportTemplateAffaire updatedRapportTemplateAffaire = rapportTemplateAffaireRepository
            .findById(rapportTemplateAffaire.getId())
            .get();
        // Disconnect from session so that the updates on updatedRapportTemplateAffaire are not directly saved in db
        em.detach(updatedRapportTemplateAffaire);
        updatedRapportTemplateAffaire.affaireId(UPDATED_AFFAIRE_ID).rapportTemplateId(UPDATED_RAPPORT_TEMPLATE_ID);

        restRapportTemplateAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRapportTemplateAffaire.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRapportTemplateAffaire))
            )
            .andExpect(status().isOk());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
        RapportTemplateAffaire testRapportTemplateAffaire = rapportTemplateAffaireList.get(rapportTemplateAffaireList.size() - 1);
        assertThat(testRapportTemplateAffaire.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testRapportTemplateAffaire.getRapportTemplateId()).isEqualTo(UPDATED_RAPPORT_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void putNonExistingRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();
        rapportTemplateAffaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportTemplateAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportTemplateAffaire.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();
        rapportTemplateAffaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportTemplateAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();
        rapportTemplateAffaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportTemplateAffaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapportTemplateAffaireWithPatch() throws Exception {
        // Initialize the database
        rapportTemplateAffaireRepository.saveAndFlush(rapportTemplateAffaire);

        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();

        // Update the rapportTemplateAffaire using partial update
        RapportTemplateAffaire partialUpdatedRapportTemplateAffaire = new RapportTemplateAffaire();
        partialUpdatedRapportTemplateAffaire.setId(rapportTemplateAffaire.getId());

        restRapportTemplateAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapportTemplateAffaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRapportTemplateAffaire))
            )
            .andExpect(status().isOk());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
        RapportTemplateAffaire testRapportTemplateAffaire = rapportTemplateAffaireList.get(rapportTemplateAffaireList.size() - 1);
        assertThat(testRapportTemplateAffaire.getAffaireId()).isEqualTo(DEFAULT_AFFAIRE_ID);
        assertThat(testRapportTemplateAffaire.getRapportTemplateId()).isEqualTo(DEFAULT_RAPPORT_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void fullUpdateRapportTemplateAffaireWithPatch() throws Exception {
        // Initialize the database
        rapportTemplateAffaireRepository.saveAndFlush(rapportTemplateAffaire);

        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();

        // Update the rapportTemplateAffaire using partial update
        RapportTemplateAffaire partialUpdatedRapportTemplateAffaire = new RapportTemplateAffaire();
        partialUpdatedRapportTemplateAffaire.setId(rapportTemplateAffaire.getId());

        partialUpdatedRapportTemplateAffaire.affaireId(UPDATED_AFFAIRE_ID).rapportTemplateId(UPDATED_RAPPORT_TEMPLATE_ID);

        restRapportTemplateAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapportTemplateAffaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRapportTemplateAffaire))
            )
            .andExpect(status().isOk());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
        RapportTemplateAffaire testRapportTemplateAffaire = rapportTemplateAffaireList.get(rapportTemplateAffaireList.size() - 1);
        assertThat(testRapportTemplateAffaire.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testRapportTemplateAffaire.getRapportTemplateId()).isEqualTo(UPDATED_RAPPORT_TEMPLATE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();
        rapportTemplateAffaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportTemplateAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rapportTemplateAffaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();
        rapportTemplateAffaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportTemplateAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRapportTemplateAffaire() throws Exception {
        int databaseSizeBeforeUpdate = rapportTemplateAffaireRepository.findAll().size();
        rapportTemplateAffaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportTemplateAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rapportTemplateAffaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RapportTemplateAffaire in the database
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRapportTemplateAffaire() throws Exception {
        // Initialize the database
        rapportTemplateAffaireRepository.saveAndFlush(rapportTemplateAffaire);

        int databaseSizeBeforeDelete = rapportTemplateAffaireRepository.findAll().size();

        // Delete the rapportTemplateAffaire
        restRapportTemplateAffaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, rapportTemplateAffaire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RapportTemplateAffaire> rapportTemplateAffaireList = rapportTemplateAffaireRepository.findAll();
        assertThat(rapportTemplateAffaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
