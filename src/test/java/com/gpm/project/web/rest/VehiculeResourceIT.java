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
import com.gpm.project.domain.Vehicule;
import com.gpm.project.domain.enumeration.StatutVehicule;
import com.gpm.project.repository.VehiculeRepository;
import com.gpm.project.service.VehiculeService;
import com.gpm.project.service.dto.VehiculeDTO;
import com.gpm.project.service.mapper.VehiculeMapper;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link VehiculeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VehiculeResourceIT {

    private static final String DEFAULT_MARQUE = "AAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_PLACES = 1;
    private static final Integer UPDATED_NB_PLACES = 2;

    private static final String DEFAULT_NUM_CARTE_GRISE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_CARTE_GRISE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CIRCULATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CIRCULATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE_CARBURANT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CARBURANT = "BBBBBBBBBB";

    private static final Float DEFAULT_CHARGE_FIXE = 1F;
    private static final Float UPDATED_CHARGE_FIXE = 2F;

    private static final Float DEFAULT_TAUX_CONSOMMATION = 1F;
    private static final Float UPDATED_TAUX_CONSOMMATION = 2F;

    private static final StatutVehicule DEFAULT_STATUT = StatutVehicule.Disponible;
    private static final StatutVehicule UPDATED_STATUT = StatutVehicule.EnMission;

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

    private static final String ENTITY_API_URL = "/api/vehicules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Mock
    private VehiculeRepository vehiculeRepositoryMock;

    @Autowired
    private VehiculeMapper vehiculeMapper;

    @Mock
    private VehiculeService vehiculeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehiculeMockMvc;

    private Vehicule vehicule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicule createEntity(EntityManager em) {
        Vehicule vehicule = new Vehicule()
            .marque(DEFAULT_MARQUE)
            .type(DEFAULT_TYPE)
            .matricule(DEFAULT_MATRICULE)
            .nbPlaces(DEFAULT_NB_PLACES)
            .numCarteGrise(DEFAULT_NUM_CARTE_GRISE)
            .dateCirculation(DEFAULT_DATE_CIRCULATION)
            .typeCarburant(DEFAULT_TYPE_CARBURANT)
            .chargeFixe(DEFAULT_CHARGE_FIXE)
            .tauxConsommation(DEFAULT_TAUX_CONSOMMATION)
            .statut(DEFAULT_STATUT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        // Add required entity
        Agence agence;
        if (TestUtil.findAll(em, Agence.class).isEmpty()) {
            agence = AgenceResourceIT.createEntity(em);
            em.persist(agence);
            em.flush();
        } else {
            agence = TestUtil.findAll(em, Agence.class).get(0);
        }
        vehicule.setAgence(agence);
        return vehicule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicule createUpdatedEntity(EntityManager em) {
        Vehicule vehicule = new Vehicule()
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .matricule(UPDATED_MATRICULE)
            .nbPlaces(UPDATED_NB_PLACES)
            .numCarteGrise(UPDATED_NUM_CARTE_GRISE)
            .dateCirculation(UPDATED_DATE_CIRCULATION)
            .typeCarburant(UPDATED_TYPE_CARBURANT)
            .chargeFixe(UPDATED_CHARGE_FIXE)
            .tauxConsommation(UPDATED_TAUX_CONSOMMATION)
            .statut(UPDATED_STATUT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        // Add required entity
        Agence agence;
        if (TestUtil.findAll(em, Agence.class).isEmpty()) {
            agence = AgenceResourceIT.createUpdatedEntity(em);
            em.persist(agence);
            em.flush();
        } else {
            agence = TestUtil.findAll(em, Agence.class).get(0);
        }
        vehicule.setAgence(agence);
        return vehicule;
    }

    @BeforeEach
    public void initTest() {
        vehicule = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicule() throws Exception {
        int databaseSizeBeforeCreate = vehiculeRepository.findAll().size();
        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);
        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testVehicule.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVehicule.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testVehicule.getNbPlaces()).isEqualTo(DEFAULT_NB_PLACES);
        assertThat(testVehicule.getNumCarteGrise()).isEqualTo(DEFAULT_NUM_CARTE_GRISE);
        assertThat(testVehicule.getDateCirculation()).isEqualTo(DEFAULT_DATE_CIRCULATION);
        assertThat(testVehicule.getTypeCarburant()).isEqualTo(DEFAULT_TYPE_CARBURANT);
        assertThat(testVehicule.getChargeFixe()).isEqualTo(DEFAULT_CHARGE_FIXE);
        assertThat(testVehicule.getTauxConsommation()).isEqualTo(DEFAULT_TAUX_CONSOMMATION);
        assertThat(testVehicule.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testVehicule.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testVehicule.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testVehicule.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVehicule.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testVehicule.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testVehicule.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createVehiculeWithExistingId() throws Exception {
        // Create the Vehicule with an existing ID
        vehicule.setId(1L);
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        int databaseSizeBeforeCreate = vehiculeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMarqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setMarque(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setType(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setMatricule(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbPlacesIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setNbPlaces(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeCarburantIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setTypeCarburant(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = vehiculeRepository.findAll().size();
        // set the field null
        vehicule.setStatut(null);

        // Create the Vehicule, which fails.
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        restVehiculeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVehicules() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        // Get all the vehiculeList
        restVehiculeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicule.getId().intValue())))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nbPlaces").value(hasItem(DEFAULT_NB_PLACES)))
            .andExpect(jsonPath("$.[*].numCarteGrise").value(hasItem(DEFAULT_NUM_CARTE_GRISE)))
            .andExpect(jsonPath("$.[*].dateCirculation").value(hasItem(DEFAULT_DATE_CIRCULATION.toString())))
            .andExpect(jsonPath("$.[*].typeCarburant").value(hasItem(DEFAULT_TYPE_CARBURANT)))
            .andExpect(jsonPath("$.[*].chargeFixe").value(hasItem(DEFAULT_CHARGE_FIXE.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxConsommation").value(hasItem(DEFAULT_TAUX_CONSOMMATION.doubleValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehiculesWithEagerRelationshipsIsEnabled() throws Exception {
        when(vehiculeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehiculeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vehiculeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVehiculesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vehiculeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVehiculeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vehiculeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        // Get the vehicule
        restVehiculeMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicule.getId().intValue()))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nbPlaces").value(DEFAULT_NB_PLACES))
            .andExpect(jsonPath("$.numCarteGrise").value(DEFAULT_NUM_CARTE_GRISE))
            .andExpect(jsonPath("$.dateCirculation").value(DEFAULT_DATE_CIRCULATION.toString()))
            .andExpect(jsonPath("$.typeCarburant").value(DEFAULT_TYPE_CARBURANT))
            .andExpect(jsonPath("$.chargeFixe").value(DEFAULT_CHARGE_FIXE.doubleValue()))
            .andExpect(jsonPath("$.tauxConsommation").value(DEFAULT_TAUX_CONSOMMATION.doubleValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingVehicule() throws Exception {
        // Get the vehicule
        restVehiculeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Update the vehicule
        Vehicule updatedVehicule = vehiculeRepository.findById(vehicule.getId()).get();
        // Disconnect from session so that the updates on updatedVehicule are not directly saved in db
        em.detach(updatedVehicule);
        updatedVehicule
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .matricule(UPDATED_MATRICULE)
            .nbPlaces(UPDATED_NB_PLACES)
            .numCarteGrise(UPDATED_NUM_CARTE_GRISE)
            .dateCirculation(UPDATED_DATE_CIRCULATION)
            .typeCarburant(UPDATED_TYPE_CARBURANT)
            .chargeFixe(UPDATED_CHARGE_FIXE)
            .tauxConsommation(UPDATED_TAUX_CONSOMMATION)
            .statut(UPDATED_STATUT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(updatedVehicule);

        restVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehiculeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testVehicule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVehicule.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testVehicule.getNbPlaces()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testVehicule.getNumCarteGrise()).isEqualTo(UPDATED_NUM_CARTE_GRISE);
        assertThat(testVehicule.getDateCirculation()).isEqualTo(UPDATED_DATE_CIRCULATION);
        assertThat(testVehicule.getTypeCarburant()).isEqualTo(UPDATED_TYPE_CARBURANT);
        assertThat(testVehicule.getChargeFixe()).isEqualTo(UPDATED_CHARGE_FIXE);
        assertThat(testVehicule.getTauxConsommation()).isEqualTo(UPDATED_TAUX_CONSOMMATION);
        assertThat(testVehicule.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testVehicule.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVehicule.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testVehicule.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVehicule.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testVehicule.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testVehicule.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();
        vehicule.setId(count.incrementAndGet());

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehiculeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();
        vehicule.setId(count.incrementAndGet());

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();
        vehicule.setId(count.incrementAndGet());

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehiculeWithPatch() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Update the vehicule using partial update
        Vehicule partialUpdatedVehicule = new Vehicule();
        partialUpdatedVehicule.setId(vehicule.getId());

        partialUpdatedVehicule
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .matricule(UPDATED_MATRICULE)
            .dateCirculation(UPDATED_DATE_CIRCULATION)
            .tauxConsommation(UPDATED_TAUX_CONSOMMATION)
            .updatedAt(UPDATED_UPDATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicule))
            )
            .andExpect(status().isOk());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testVehicule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVehicule.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testVehicule.getNbPlaces()).isEqualTo(DEFAULT_NB_PLACES);
        assertThat(testVehicule.getNumCarteGrise()).isEqualTo(DEFAULT_NUM_CARTE_GRISE);
        assertThat(testVehicule.getDateCirculation()).isEqualTo(UPDATED_DATE_CIRCULATION);
        assertThat(testVehicule.getTypeCarburant()).isEqualTo(DEFAULT_TYPE_CARBURANT);
        assertThat(testVehicule.getChargeFixe()).isEqualTo(DEFAULT_CHARGE_FIXE);
        assertThat(testVehicule.getTauxConsommation()).isEqualTo(UPDATED_TAUX_CONSOMMATION);
        assertThat(testVehicule.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testVehicule.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testVehicule.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testVehicule.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVehicule.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testVehicule.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testVehicule.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateVehiculeWithPatch() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();

        // Update the vehicule using partial update
        Vehicule partialUpdatedVehicule = new Vehicule();
        partialUpdatedVehicule.setId(vehicule.getId());

        partialUpdatedVehicule
            .marque(UPDATED_MARQUE)
            .type(UPDATED_TYPE)
            .matricule(UPDATED_MATRICULE)
            .nbPlaces(UPDATED_NB_PLACES)
            .numCarteGrise(UPDATED_NUM_CARTE_GRISE)
            .dateCirculation(UPDATED_DATE_CIRCULATION)
            .typeCarburant(UPDATED_TYPE_CARBURANT)
            .chargeFixe(UPDATED_CHARGE_FIXE)
            .tauxConsommation(UPDATED_TAUX_CONSOMMATION)
            .statut(UPDATED_STATUT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicule))
            )
            .andExpect(status().isOk());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
        Vehicule testVehicule = vehiculeList.get(vehiculeList.size() - 1);
        assertThat(testVehicule.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testVehicule.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVehicule.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testVehicule.getNbPlaces()).isEqualTo(UPDATED_NB_PLACES);
        assertThat(testVehicule.getNumCarteGrise()).isEqualTo(UPDATED_NUM_CARTE_GRISE);
        assertThat(testVehicule.getDateCirculation()).isEqualTo(UPDATED_DATE_CIRCULATION);
        assertThat(testVehicule.getTypeCarburant()).isEqualTo(UPDATED_TYPE_CARBURANT);
        assertThat(testVehicule.getChargeFixe()).isEqualTo(UPDATED_CHARGE_FIXE);
        assertThat(testVehicule.getTauxConsommation()).isEqualTo(UPDATED_TAUX_CONSOMMATION);
        assertThat(testVehicule.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testVehicule.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVehicule.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testVehicule.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVehicule.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testVehicule.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testVehicule.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();
        vehicule.setId(count.incrementAndGet());

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehiculeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();
        vehicule.setId(count.incrementAndGet());

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicule() throws Exception {
        int databaseSizeBeforeUpdate = vehiculeRepository.findAll().size();
        vehicule.setId(count.incrementAndGet());

        // Create the Vehicule
        VehiculeDTO vehiculeDTO = vehiculeMapper.toDto(vehicule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehiculeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehiculeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicule in the database
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicule() throws Exception {
        // Initialize the database
        vehiculeRepository.saveAndFlush(vehicule);

        int databaseSizeBeforeDelete = vehiculeRepository.findAll().size();

        // Delete the vehicule
        restVehiculeMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicule.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicule> vehiculeList = vehiculeRepository.findAll();
        assertThat(vehiculeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
