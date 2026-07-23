package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.AffaireSocieteAdj;
import com.gpm.project.repository.AffaireSocieteAdjRepository;
import com.gpm.project.service.dto.AffaireSocieteAdjDTO;
import com.gpm.project.service.mapper.AffaireSocieteAdjMapper;
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
 * Integration tests for the {@link AffaireSocieteAdjResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffaireSocieteAdjResourceIT {

    private static final Long DEFAULT_AFFAIRE_ID = 1L;
    private static final Long UPDATED_AFFAIRE_ID = 2L;

    private static final Long DEFAULT_SOCIETE_ID = 1L;
    private static final Long UPDATED_SOCIETE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/affaire-societe-adjs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffaireSocieteAdjRepository affaireSocieteAdjRepository;

    @Autowired
    private AffaireSocieteAdjMapper affaireSocieteAdjMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffaireSocieteAdjMockMvc;

    private AffaireSocieteAdj affaireSocieteAdj;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffaireSocieteAdj createEntity(EntityManager em) {
        AffaireSocieteAdj affaireSocieteAdj = new AffaireSocieteAdj().affaireId(DEFAULT_AFFAIRE_ID).societeId(DEFAULT_SOCIETE_ID);
        return affaireSocieteAdj;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffaireSocieteAdj createUpdatedEntity(EntityManager em) {
        AffaireSocieteAdj affaireSocieteAdj = new AffaireSocieteAdj().affaireId(UPDATED_AFFAIRE_ID).societeId(UPDATED_SOCIETE_ID);
        return affaireSocieteAdj;
    }

    @BeforeEach
    public void initTest() {
        affaireSocieteAdj = createEntity(em);
    }

    @Test
    @Transactional
    void createAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeCreate = affaireSocieteAdjRepository.findAll().size();
        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);
        restAffaireSocieteAdjMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeCreate + 1);
        AffaireSocieteAdj testAffaireSocieteAdj = affaireSocieteAdjList.get(affaireSocieteAdjList.size() - 1);
        assertThat(testAffaireSocieteAdj.getAffaireId()).isEqualTo(DEFAULT_AFFAIRE_ID);
        assertThat(testAffaireSocieteAdj.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void createAffaireSocieteAdjWithExistingId() throws Exception {
        // Create the AffaireSocieteAdj with an existing ID
        affaireSocieteAdj.setId(1L);
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        int databaseSizeBeforeCreate = affaireSocieteAdjRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffaireSocieteAdjMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAffaireSocieteAdjs() throws Exception {
        // Initialize the database
        affaireSocieteAdjRepository.saveAndFlush(affaireSocieteAdj);

        // Get all the affaireSocieteAdjList
        restAffaireSocieteAdjMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affaireSocieteAdj.getId().intValue())))
            .andExpect(jsonPath("$.[*].affaireId").value(hasItem(DEFAULT_AFFAIRE_ID.intValue())))
            .andExpect(jsonPath("$.[*].societeId").value(hasItem(DEFAULT_SOCIETE_ID.intValue())));
    }

    @Test
    @Transactional
    void getAffaireSocieteAdj() throws Exception {
        // Initialize the database
        affaireSocieteAdjRepository.saveAndFlush(affaireSocieteAdj);

        // Get the affaireSocieteAdj
        restAffaireSocieteAdjMockMvc
            .perform(get(ENTITY_API_URL_ID, affaireSocieteAdj.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affaireSocieteAdj.getId().intValue()))
            .andExpect(jsonPath("$.affaireId").value(DEFAULT_AFFAIRE_ID.intValue()))
            .andExpect(jsonPath("$.societeId").value(DEFAULT_SOCIETE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAffaireSocieteAdj() throws Exception {
        // Get the affaireSocieteAdj
        restAffaireSocieteAdjMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAffaireSocieteAdj() throws Exception {
        // Initialize the database
        affaireSocieteAdjRepository.saveAndFlush(affaireSocieteAdj);

        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();

        // Update the affaireSocieteAdj
        AffaireSocieteAdj updatedAffaireSocieteAdj = affaireSocieteAdjRepository.findById(affaireSocieteAdj.getId()).get();
        // Disconnect from session so that the updates on updatedAffaireSocieteAdj are not directly saved in db
        em.detach(updatedAffaireSocieteAdj);
        updatedAffaireSocieteAdj.affaireId(UPDATED_AFFAIRE_ID).societeId(UPDATED_SOCIETE_ID);
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(updatedAffaireSocieteAdj);

        restAffaireSocieteAdjMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaireSocieteAdjDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isOk());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
        AffaireSocieteAdj testAffaireSocieteAdj = affaireSocieteAdjList.get(affaireSocieteAdjList.size() - 1);
        assertThat(testAffaireSocieteAdj.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testAffaireSocieteAdj.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
    }

    @Test
    @Transactional
    void putNonExistingAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();
        affaireSocieteAdj.setId(count.incrementAndGet());

        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireSocieteAdjMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaireSocieteAdjDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();
        affaireSocieteAdj.setId(count.incrementAndGet());

        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireSocieteAdjMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();
        affaireSocieteAdj.setId(count.incrementAndGet());

        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireSocieteAdjMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffaireSocieteAdjWithPatch() throws Exception {
        // Initialize the database
        affaireSocieteAdjRepository.saveAndFlush(affaireSocieteAdj);

        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();

        // Update the affaireSocieteAdj using partial update
        AffaireSocieteAdj partialUpdatedAffaireSocieteAdj = new AffaireSocieteAdj();
        partialUpdatedAffaireSocieteAdj.setId(affaireSocieteAdj.getId());

        partialUpdatedAffaireSocieteAdj.affaireId(UPDATED_AFFAIRE_ID).societeId(UPDATED_SOCIETE_ID);

        restAffaireSocieteAdjMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaireSocieteAdj.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaireSocieteAdj))
            )
            .andExpect(status().isOk());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
        AffaireSocieteAdj testAffaireSocieteAdj = affaireSocieteAdjList.get(affaireSocieteAdjList.size() - 1);
        assertThat(testAffaireSocieteAdj.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testAffaireSocieteAdj.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
    }

    @Test
    @Transactional
    void fullUpdateAffaireSocieteAdjWithPatch() throws Exception {
        // Initialize the database
        affaireSocieteAdjRepository.saveAndFlush(affaireSocieteAdj);

        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();

        // Update the affaireSocieteAdj using partial update
        AffaireSocieteAdj partialUpdatedAffaireSocieteAdj = new AffaireSocieteAdj();
        partialUpdatedAffaireSocieteAdj.setId(affaireSocieteAdj.getId());

        partialUpdatedAffaireSocieteAdj.affaireId(UPDATED_AFFAIRE_ID).societeId(UPDATED_SOCIETE_ID);

        restAffaireSocieteAdjMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaireSocieteAdj.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaireSocieteAdj))
            )
            .andExpect(status().isOk());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
        AffaireSocieteAdj testAffaireSocieteAdj = affaireSocieteAdjList.get(affaireSocieteAdjList.size() - 1);
        assertThat(testAffaireSocieteAdj.getAffaireId()).isEqualTo(UPDATED_AFFAIRE_ID);
        assertThat(testAffaireSocieteAdj.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();
        affaireSocieteAdj.setId(count.incrementAndGet());

        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireSocieteAdjMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affaireSocieteAdjDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();
        affaireSocieteAdj.setId(count.incrementAndGet());

        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireSocieteAdjMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffaireSocieteAdj() throws Exception {
        int databaseSizeBeforeUpdate = affaireSocieteAdjRepository.findAll().size();
        affaireSocieteAdj.setId(count.incrementAndGet());

        // Create the AffaireSocieteAdj
        AffaireSocieteAdjDTO affaireSocieteAdjDTO = affaireSocieteAdjMapper.toDto(affaireSocieteAdj);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireSocieteAdjMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireSocieteAdjDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffaireSocieteAdj in the database
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffaireSocieteAdj() throws Exception {
        // Initialize the database
        affaireSocieteAdjRepository.saveAndFlush(affaireSocieteAdj);

        int databaseSizeBeforeDelete = affaireSocieteAdjRepository.findAll().size();

        // Delete the affaireSocieteAdj
        restAffaireSocieteAdjMockMvc
            .perform(delete(ENTITY_API_URL_ID, affaireSocieteAdj.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AffaireSocieteAdj> affaireSocieteAdjList = affaireSocieteAdjRepository.findAll();
        assertThat(affaireSocieteAdjList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
