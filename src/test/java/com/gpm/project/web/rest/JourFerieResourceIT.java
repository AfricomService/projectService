package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.JourFerie;
import com.gpm.project.repository.JourFerieRepository;
import com.gpm.project.service.dto.JourFerieDTO;
import com.gpm.project.service.mapper.JourFerieMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link JourFerieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JourFerieResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE = 1;
    private static final Integer UPDATED_ANNEE = 2;

    private static final String ENTITY_API_URL = "/api/jour-feries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JourFerieRepository jourFerieRepository;

    @Autowired
    private JourFerieMapper jourFerieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJourFerieMockMvc;

    private JourFerie jourFerie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JourFerie createEntity(EntityManager em) {
        JourFerie jourFerie = new JourFerie().date(DEFAULT_DATE).designation(DEFAULT_DESIGNATION).annee(DEFAULT_ANNEE);
        return jourFerie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JourFerie createUpdatedEntity(EntityManager em) {
        JourFerie jourFerie = new JourFerie().date(UPDATED_DATE).designation(UPDATED_DESIGNATION).annee(UPDATED_ANNEE);
        return jourFerie;
    }

    @BeforeEach
    public void initTest() {
        jourFerie = createEntity(em);
    }

    @Test
    @Transactional
    void createJourFerie() throws Exception {
        int databaseSizeBeforeCreate = jourFerieRepository.findAll().size();
        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);
        restJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isCreated());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeCreate + 1);
        JourFerie testJourFerie = jourFerieList.get(jourFerieList.size() - 1);
        assertThat(testJourFerie.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testJourFerie.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testJourFerie.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void createJourFerieWithExistingId() throws Exception {
        // Create the JourFerie with an existing ID
        jourFerie.setId(1L);
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        int databaseSizeBeforeCreate = jourFerieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = jourFerieRepository.findAll().size();
        // set the field null
        jourFerie.setDate(null);

        // Create the JourFerie, which fails.
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        restJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = jourFerieRepository.findAll().size();
        // set the field null
        jourFerie.setDesignation(null);

        // Create the JourFerie, which fails.
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        restJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jourFerieRepository.findAll().size();
        // set the field null
        jourFerie.setAnnee(null);

        // Create the JourFerie, which fails.
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        restJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJourFeries() throws Exception {
        // Initialize the database
        jourFerieRepository.saveAndFlush(jourFerie);

        // Get all the jourFerieList
        restJourFerieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jourFerie.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)));
    }

    @Test
    @Transactional
    void getJourFerie() throws Exception {
        // Initialize the database
        jourFerieRepository.saveAndFlush(jourFerie);

        // Get the jourFerie
        restJourFerieMockMvc
            .perform(get(ENTITY_API_URL_ID, jourFerie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jourFerie.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE));
    }

    @Test
    @Transactional
    void getNonExistingJourFerie() throws Exception {
        // Get the jourFerie
        restJourFerieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJourFerie() throws Exception {
        // Initialize the database
        jourFerieRepository.saveAndFlush(jourFerie);

        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();

        // Update the jourFerie
        JourFerie updatedJourFerie = jourFerieRepository.findById(jourFerie.getId()).get();
        // Disconnect from session so that the updates on updatedJourFerie are not directly saved in db
        em.detach(updatedJourFerie);
        updatedJourFerie.date(UPDATED_DATE).designation(UPDATED_DESIGNATION).annee(UPDATED_ANNEE);
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(updatedJourFerie);

        restJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jourFerieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isOk());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
        JourFerie testJourFerie = jourFerieList.get(jourFerieList.size() - 1);
        assertThat(testJourFerie.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJourFerie.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testJourFerie.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void putNonExistingJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();
        jourFerie.setId(count.incrementAndGet());

        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jourFerieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();
        jourFerie.setId(count.incrementAndGet());

        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();
        jourFerie.setId(count.incrementAndGet());

        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJourFerieWithPatch() throws Exception {
        // Initialize the database
        jourFerieRepository.saveAndFlush(jourFerie);

        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();

        // Update the jourFerie using partial update
        JourFerie partialUpdatedJourFerie = new JourFerie();
        partialUpdatedJourFerie.setId(jourFerie.getId());

        partialUpdatedJourFerie.date(UPDATED_DATE).designation(UPDATED_DESIGNATION);

        restJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJourFerie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJourFerie))
            )
            .andExpect(status().isOk());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
        JourFerie testJourFerie = jourFerieList.get(jourFerieList.size() - 1);
        assertThat(testJourFerie.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJourFerie.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testJourFerie.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void fullUpdateJourFerieWithPatch() throws Exception {
        // Initialize the database
        jourFerieRepository.saveAndFlush(jourFerie);

        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();

        // Update the jourFerie using partial update
        JourFerie partialUpdatedJourFerie = new JourFerie();
        partialUpdatedJourFerie.setId(jourFerie.getId());

        partialUpdatedJourFerie.date(UPDATED_DATE).designation(UPDATED_DESIGNATION).annee(UPDATED_ANNEE);

        restJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJourFerie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJourFerie))
            )
            .andExpect(status().isOk());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
        JourFerie testJourFerie = jourFerieList.get(jourFerieList.size() - 1);
        assertThat(testJourFerie.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testJourFerie.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testJourFerie.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void patchNonExistingJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();
        jourFerie.setId(count.incrementAndGet());

        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jourFerieDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();
        jourFerie.setId(count.incrementAndGet());

        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = jourFerieRepository.findAll().size();
        jourFerie.setId(count.incrementAndGet());

        // Create the JourFerie
        JourFerieDTO jourFerieDTO = jourFerieMapper.toDto(jourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jourFerieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JourFerie in the database
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJourFerie() throws Exception {
        // Initialize the database
        jourFerieRepository.saveAndFlush(jourFerie);

        int databaseSizeBeforeDelete = jourFerieRepository.findAll().size();

        // Delete the jourFerie
        restJourFerieMockMvc
            .perform(delete(ENTITY_API_URL_ID, jourFerie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JourFerie> jourFerieList = jourFerieRepository.findAll();
        assertThat(jourFerieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
