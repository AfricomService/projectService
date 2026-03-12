package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.MatriceFacturation;
import com.gpm.project.repository.MatriceFacturationRepository;
import com.gpm.project.service.MatriceFacturationService;
import com.gpm.project.service.dto.MatriceFacturationDTO;
import com.gpm.project.service.mapper.MatriceFacturationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MatriceFacturationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatriceFacturationResourceIT {

    private static final Float DEFAULT_TARIF_BASE = 1F;
    private static final Float UPDATED_TARIF_BASE = 2F;

    private static final Float DEFAULT_TARIF_MISSION_NUIT = 1F;
    private static final Float UPDATED_TARIF_MISSION_NUIT = 2F;

    private static final Float DEFAULT_TARIF_HEBERGEMENT = 1F;
    private static final Float UPDATED_TARIF_HEBERGEMENT = 2F;

    private static final Float DEFAULT_TARIF_JOUR_FERIE = 1F;
    private static final Float UPDATED_TARIF_JOUR_FERIE = 2F;

    private static final Float DEFAULT_TARIF_DIMANCHE = 1F;
    private static final Float UPDATED_TARIF_DIMANCHE = 2F;

    private static final String ENTITY_API_URL = "/api/matrice-facturations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatriceFacturationRepository matriceFacturationRepository;

    @Mock
    private MatriceFacturationRepository matriceFacturationRepositoryMock;

    @Autowired
    private MatriceFacturationMapper matriceFacturationMapper;

    @Mock
    private MatriceFacturationService matriceFacturationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatriceFacturationMockMvc;

    private MatriceFacturation matriceFacturation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatriceFacturation createEntity(EntityManager em) {
        MatriceFacturation matriceFacturation = new MatriceFacturation()
            .tarifBase(DEFAULT_TARIF_BASE)
            .tarifMissionNuit(DEFAULT_TARIF_MISSION_NUIT)
            .tarifHebergement(DEFAULT_TARIF_HEBERGEMENT)
            .tarifJourFerie(DEFAULT_TARIF_JOUR_FERIE)
            .tarifDimanche(DEFAULT_TARIF_DIMANCHE);
        // Add required entity
        Affaire affaire;
        if (TestUtil.findAll(em, Affaire.class).isEmpty()) {
            affaire = AffaireResourceIT.createEntity(em);
            em.persist(affaire);
            em.flush();
        } else {
            affaire = TestUtil.findAll(em, Affaire.class).get(0);
        }
        matriceFacturation.setAffaire(affaire);
        return matriceFacturation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatriceFacturation createUpdatedEntity(EntityManager em) {
        MatriceFacturation matriceFacturation = new MatriceFacturation()
            .tarifBase(UPDATED_TARIF_BASE)
            .tarifMissionNuit(UPDATED_TARIF_MISSION_NUIT)
            .tarifHebergement(UPDATED_TARIF_HEBERGEMENT)
            .tarifJourFerie(UPDATED_TARIF_JOUR_FERIE)
            .tarifDimanche(UPDATED_TARIF_DIMANCHE);
        // Add required entity
        Affaire affaire;
        if (TestUtil.findAll(em, Affaire.class).isEmpty()) {
            affaire = AffaireResourceIT.createUpdatedEntity(em);
            em.persist(affaire);
            em.flush();
        } else {
            affaire = TestUtil.findAll(em, Affaire.class).get(0);
        }
        matriceFacturation.setAffaire(affaire);
        return matriceFacturation;
    }

    @BeforeEach
    public void initTest() {
        matriceFacturation = createEntity(em);
    }

    @Test
    @Transactional
    void createMatriceFacturation() throws Exception {
        int databaseSizeBeforeCreate = matriceFacturationRepository.findAll().size();
        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);
        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeCreate + 1);
        MatriceFacturation testMatriceFacturation = matriceFacturationList.get(matriceFacturationList.size() - 1);
        assertThat(testMatriceFacturation.getTarifBase()).isEqualTo(DEFAULT_TARIF_BASE);
        assertThat(testMatriceFacturation.getTarifMissionNuit()).isEqualTo(DEFAULT_TARIF_MISSION_NUIT);
        assertThat(testMatriceFacturation.getTarifHebergement()).isEqualTo(DEFAULT_TARIF_HEBERGEMENT);
        assertThat(testMatriceFacturation.getTarifJourFerie()).isEqualTo(DEFAULT_TARIF_JOUR_FERIE);
        assertThat(testMatriceFacturation.getTarifDimanche()).isEqualTo(DEFAULT_TARIF_DIMANCHE);
    }

    @Test
    @Transactional
    void createMatriceFacturationWithExistingId() throws Exception {
        // Create the MatriceFacturation with an existing ID
        matriceFacturation.setId(1L);
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        int databaseSizeBeforeCreate = matriceFacturationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTarifBaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriceFacturationRepository.findAll().size();
        // set the field null
        matriceFacturation.setTarifBase(null);

        // Create the MatriceFacturation, which fails.
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTarifMissionNuitIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriceFacturationRepository.findAll().size();
        // set the field null
        matriceFacturation.setTarifMissionNuit(null);

        // Create the MatriceFacturation, which fails.
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTarifHebergementIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriceFacturationRepository.findAll().size();
        // set the field null
        matriceFacturation.setTarifHebergement(null);

        // Create the MatriceFacturation, which fails.
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTarifJourFerieIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriceFacturationRepository.findAll().size();
        // set the field null
        matriceFacturation.setTarifJourFerie(null);

        // Create the MatriceFacturation, which fails.
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTarifDimancheIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriceFacturationRepository.findAll().size();
        // set the field null
        matriceFacturation.setTarifDimanche(null);

        // Create the MatriceFacturation, which fails.
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        restMatriceFacturationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatriceFacturations() throws Exception {
        // Initialize the database
        matriceFacturationRepository.saveAndFlush(matriceFacturation);

        // Get all the matriceFacturationList
        restMatriceFacturationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matriceFacturation.getId().intValue())))
            .andExpect(jsonPath("$.[*].tarifBase").value(hasItem(DEFAULT_TARIF_BASE.doubleValue())))
            .andExpect(jsonPath("$.[*].tarifMissionNuit").value(hasItem(DEFAULT_TARIF_MISSION_NUIT.doubleValue())))
            .andExpect(jsonPath("$.[*].tarifHebergement").value(hasItem(DEFAULT_TARIF_HEBERGEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].tarifJourFerie").value(hasItem(DEFAULT_TARIF_JOUR_FERIE.doubleValue())))
            .andExpect(jsonPath("$.[*].tarifDimanche").value(hasItem(DEFAULT_TARIF_DIMANCHE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriceFacturationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(matriceFacturationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriceFacturationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matriceFacturationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriceFacturationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matriceFacturationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriceFacturationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(matriceFacturationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMatriceFacturation() throws Exception {
        // Initialize the database
        matriceFacturationRepository.saveAndFlush(matriceFacturation);

        // Get the matriceFacturation
        restMatriceFacturationMockMvc
            .perform(get(ENTITY_API_URL_ID, matriceFacturation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matriceFacturation.getId().intValue()))
            .andExpect(jsonPath("$.tarifBase").value(DEFAULT_TARIF_BASE.doubleValue()))
            .andExpect(jsonPath("$.tarifMissionNuit").value(DEFAULT_TARIF_MISSION_NUIT.doubleValue()))
            .andExpect(jsonPath("$.tarifHebergement").value(DEFAULT_TARIF_HEBERGEMENT.doubleValue()))
            .andExpect(jsonPath("$.tarifJourFerie").value(DEFAULT_TARIF_JOUR_FERIE.doubleValue()))
            .andExpect(jsonPath("$.tarifDimanche").value(DEFAULT_TARIF_DIMANCHE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMatriceFacturation() throws Exception {
        // Get the matriceFacturation
        restMatriceFacturationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatriceFacturation() throws Exception {
        // Initialize the database
        matriceFacturationRepository.saveAndFlush(matriceFacturation);

        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();

        // Update the matriceFacturation
        MatriceFacturation updatedMatriceFacturation = matriceFacturationRepository.findById(matriceFacturation.getId()).get();
        // Disconnect from session so that the updates on updatedMatriceFacturation are not directly saved in db
        em.detach(updatedMatriceFacturation);
        updatedMatriceFacturation
            .tarifBase(UPDATED_TARIF_BASE)
            .tarifMissionNuit(UPDATED_TARIF_MISSION_NUIT)
            .tarifHebergement(UPDATED_TARIF_HEBERGEMENT)
            .tarifJourFerie(UPDATED_TARIF_JOUR_FERIE)
            .tarifDimanche(UPDATED_TARIF_DIMANCHE);
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(updatedMatriceFacturation);

        restMatriceFacturationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriceFacturationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isOk());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
        MatriceFacturation testMatriceFacturation = matriceFacturationList.get(matriceFacturationList.size() - 1);
        assertThat(testMatriceFacturation.getTarifBase()).isEqualTo(UPDATED_TARIF_BASE);
        assertThat(testMatriceFacturation.getTarifMissionNuit()).isEqualTo(UPDATED_TARIF_MISSION_NUIT);
        assertThat(testMatriceFacturation.getTarifHebergement()).isEqualTo(UPDATED_TARIF_HEBERGEMENT);
        assertThat(testMatriceFacturation.getTarifJourFerie()).isEqualTo(UPDATED_TARIF_JOUR_FERIE);
        assertThat(testMatriceFacturation.getTarifDimanche()).isEqualTo(UPDATED_TARIF_DIMANCHE);
    }

    @Test
    @Transactional
    void putNonExistingMatriceFacturation() throws Exception {
        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();
        matriceFacturation.setId(count.incrementAndGet());

        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriceFacturationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriceFacturationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatriceFacturation() throws Exception {
        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();
        matriceFacturation.setId(count.incrementAndGet());

        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceFacturationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatriceFacturation() throws Exception {
        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();
        matriceFacturation.setId(count.incrementAndGet());

        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceFacturationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatriceFacturationWithPatch() throws Exception {
        // Initialize the database
        matriceFacturationRepository.saveAndFlush(matriceFacturation);

        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();

        // Update the matriceFacturation using partial update
        MatriceFacturation partialUpdatedMatriceFacturation = new MatriceFacturation();
        partialUpdatedMatriceFacturation.setId(matriceFacturation.getId());

        partialUpdatedMatriceFacturation.tarifHebergement(UPDATED_TARIF_HEBERGEMENT).tarifJourFerie(UPDATED_TARIF_JOUR_FERIE);

        restMatriceFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatriceFacturation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatriceFacturation))
            )
            .andExpect(status().isOk());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
        MatriceFacturation testMatriceFacturation = matriceFacturationList.get(matriceFacturationList.size() - 1);
        assertThat(testMatriceFacturation.getTarifBase()).isEqualTo(DEFAULT_TARIF_BASE);
        assertThat(testMatriceFacturation.getTarifMissionNuit()).isEqualTo(DEFAULT_TARIF_MISSION_NUIT);
        assertThat(testMatriceFacturation.getTarifHebergement()).isEqualTo(UPDATED_TARIF_HEBERGEMENT);
        assertThat(testMatriceFacturation.getTarifJourFerie()).isEqualTo(UPDATED_TARIF_JOUR_FERIE);
        assertThat(testMatriceFacturation.getTarifDimanche()).isEqualTo(DEFAULT_TARIF_DIMANCHE);
    }

    @Test
    @Transactional
    void fullUpdateMatriceFacturationWithPatch() throws Exception {
        // Initialize the database
        matriceFacturationRepository.saveAndFlush(matriceFacturation);

        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();

        // Update the matriceFacturation using partial update
        MatriceFacturation partialUpdatedMatriceFacturation = new MatriceFacturation();
        partialUpdatedMatriceFacturation.setId(matriceFacturation.getId());

        partialUpdatedMatriceFacturation
            .tarifBase(UPDATED_TARIF_BASE)
            .tarifMissionNuit(UPDATED_TARIF_MISSION_NUIT)
            .tarifHebergement(UPDATED_TARIF_HEBERGEMENT)
            .tarifJourFerie(UPDATED_TARIF_JOUR_FERIE)
            .tarifDimanche(UPDATED_TARIF_DIMANCHE);

        restMatriceFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatriceFacturation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatriceFacturation))
            )
            .andExpect(status().isOk());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
        MatriceFacturation testMatriceFacturation = matriceFacturationList.get(matriceFacturationList.size() - 1);
        assertThat(testMatriceFacturation.getTarifBase()).isEqualTo(UPDATED_TARIF_BASE);
        assertThat(testMatriceFacturation.getTarifMissionNuit()).isEqualTo(UPDATED_TARIF_MISSION_NUIT);
        assertThat(testMatriceFacturation.getTarifHebergement()).isEqualTo(UPDATED_TARIF_HEBERGEMENT);
        assertThat(testMatriceFacturation.getTarifJourFerie()).isEqualTo(UPDATED_TARIF_JOUR_FERIE);
        assertThat(testMatriceFacturation.getTarifDimanche()).isEqualTo(UPDATED_TARIF_DIMANCHE);
    }

    @Test
    @Transactional
    void patchNonExistingMatriceFacturation() throws Exception {
        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();
        matriceFacturation.setId(count.incrementAndGet());

        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriceFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matriceFacturationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatriceFacturation() throws Exception {
        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();
        matriceFacturation.setId(count.incrementAndGet());

        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatriceFacturation() throws Exception {
        int databaseSizeBeforeUpdate = matriceFacturationRepository.findAll().size();
        matriceFacturation.setId(count.incrementAndGet());

        // Create the MatriceFacturation
        MatriceFacturationDTO matriceFacturationDTO = matriceFacturationMapper.toDto(matriceFacturation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceFacturationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matriceFacturationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatriceFacturation in the database
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatriceFacturation() throws Exception {
        // Initialize the database
        matriceFacturationRepository.saveAndFlush(matriceFacturation);

        int databaseSizeBeforeDelete = matriceFacturationRepository.findAll().size();

        // Delete the matriceFacturation
        restMatriceFacturationMockMvc
            .perform(delete(ENTITY_API_URL_ID, matriceFacturation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatriceFacturation> matriceFacturationList = matriceFacturationRepository.findAll();
        assertThat(matriceFacturationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
