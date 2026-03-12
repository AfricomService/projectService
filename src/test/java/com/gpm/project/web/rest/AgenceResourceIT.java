package com.gpm.project.web.rest;

import static com.gpm.project.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Agence;
import com.gpm.project.domain.Societe;
import com.gpm.project.repository.AgenceRepository;
import com.gpm.project.service.AgenceService;
import com.gpm.project.service.dto.AgenceDTO;
import com.gpm.project.service.mapper.AgenceMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link AgenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AgenceResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgenceRepository agenceRepository;

    @Mock
    private AgenceRepository agenceRepositoryMock;

    @Autowired
    private AgenceMapper agenceMapper;

    @Mock
    private AgenceService agenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgenceMockMvc;

    private Agence agence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agence createEntity(EntityManager em) {
        Agence agence = new Agence()
            .designation(DEFAULT_DESIGNATION)
            .adresse(DEFAULT_ADRESSE)
            .ville(DEFAULT_VILLE)
            .pays(DEFAULT_PAYS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        // Add required entity
        Societe societe;
        if (TestUtil.findAll(em, Societe.class).isEmpty()) {
            societe = SocieteResourceIT.createEntity(em);
            em.persist(societe);
            em.flush();
        } else {
            societe = TestUtil.findAll(em, Societe.class).get(0);
        }
        agence.setSociete(societe);
        return agence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agence createUpdatedEntity(EntityManager em) {
        Agence agence = new Agence()
            .designation(UPDATED_DESIGNATION)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        // Add required entity
        Societe societe;
        if (TestUtil.findAll(em, Societe.class).isEmpty()) {
            societe = SocieteResourceIT.createUpdatedEntity(em);
            em.persist(societe);
            em.flush();
        } else {
            societe = TestUtil.findAll(em, Societe.class).get(0);
        }
        agence.setSociete(societe);
        return agence;
    }

    @BeforeEach
    public void initTest() {
        agence = createEntity(em);
    }

    @Test
    @Transactional
    void createAgence() throws Exception {
        int databaseSizeBeforeCreate = agenceRepository.findAll().size();
        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);
        restAgenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeCreate + 1);
        Agence testAgence = agenceList.get(agenceList.size() - 1);
        assertThat(testAgence.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testAgence.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgence.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testAgence.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testAgence.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAgence.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testAgence.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAgence.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testAgence.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testAgence.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createAgenceWithExistingId() throws Exception {
        // Create the Agence with an existing ID
        agence.setId(1L);
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        int databaseSizeBeforeCreate = agenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenceRepository.findAll().size();
        // set the field null
        agence.setDesignation(null);

        // Create the Agence, which fails.
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        restAgenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenceRepository.findAll().size();
        // set the field null
        agence.setAdresse(null);

        // Create the Agence, which fails.
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        restAgenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenceRepository.findAll().size();
        // set the field null
        agence.setVille(null);

        // Create the Agence, which fails.
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        restAgenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = agenceRepository.findAll().size();
        // set the field null
        agence.setPays(null);

        // Create the Agence, which fails.
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        restAgenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgences() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get all the agenceList
        restAgenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agence.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(agenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(agenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAgence() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        // Get the agence
        restAgenceMockMvc
            .perform(get(ENTITY_API_URL_ID, agence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agence.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingAgence() throws Exception {
        // Get the agence
        restAgenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgence() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();

        // Update the agence
        Agence updatedAgence = agenceRepository.findById(agence.getId()).get();
        // Disconnect from session so that the updates on updatedAgence are not directly saved in db
        em.detach(updatedAgence);
        updatedAgence
            .designation(UPDATED_DESIGNATION)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        AgenceDTO agenceDTO = agenceMapper.toDto(updatedAgence);

        restAgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
        Agence testAgence = agenceList.get(agenceList.size() - 1);
        assertThat(testAgence.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testAgence.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgence.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAgence.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testAgence.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAgence.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAgence.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAgence.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testAgence.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testAgence.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();
        agence.setId(count.incrementAndGet());

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();
        agence.setId(count.incrementAndGet());

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();
        agence.setId(count.incrementAndGet());

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgenceWithPatch() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();

        // Update the agence using partial update
        Agence partialUpdatedAgence = new Agence();
        partialUpdatedAgence.setId(agence.getId());

        partialUpdatedAgence.ville(UPDATED_VILLE).pays(UPDATED_PAYS);

        restAgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgence.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgence))
            )
            .andExpect(status().isOk());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
        Agence testAgence = agenceList.get(agenceList.size() - 1);
        assertThat(testAgence.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testAgence.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testAgence.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAgence.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testAgence.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAgence.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testAgence.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAgence.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testAgence.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testAgence.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateAgenceWithPatch() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();

        // Update the agence using partial update
        Agence partialUpdatedAgence = new Agence();
        partialUpdatedAgence.setId(agence.getId());

        partialUpdatedAgence
            .designation(UPDATED_DESIGNATION)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .pays(UPDATED_PAYS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restAgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgence.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgence))
            )
            .andExpect(status().isOk());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
        Agence testAgence = agenceList.get(agenceList.size() - 1);
        assertThat(testAgence.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testAgence.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testAgence.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAgence.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testAgence.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAgence.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAgence.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAgence.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testAgence.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testAgence.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();
        agence.setId(count.incrementAndGet());

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agenceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();
        agence.setId(count.incrementAndGet());

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgence() throws Exception {
        int databaseSizeBeforeUpdate = agenceRepository.findAll().size();
        agence.setId(count.incrementAndGet());

        // Create the Agence
        AgenceDTO agenceDTO = agenceMapper.toDto(agence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agence in the database
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgence() throws Exception {
        // Initialize the database
        agenceRepository.saveAndFlush(agence);

        int databaseSizeBeforeDelete = agenceRepository.findAll().size();

        // Delete the agence
        restAgenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, agence.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agence> agenceList = agenceRepository.findAll();
        assertThat(agenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
