package com.gpm.project.web.rest;

import static com.gpm.project.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.Client;
import com.gpm.project.domain.enumeration.StatutAffaire;
import com.gpm.project.repository.AffaireRepository;
import com.gpm.project.service.AffaireService;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.mapper.AffaireMapper;
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
 * Integration tests for the {@link AffaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AffaireResourceIT {

    private static final Integer DEFAULT_NUM_AFFAIRE = 1;
    private static final Integer UPDATED_NUM_AFFAIRE = 2;

    private static final String DEFAULT_DESIGNATION_AFFAIRE = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_AFFAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_BON_DE_COMMANDE = "AAAAAAAAAA";
    private static final String UPDATED_BON_DE_COMMANDE = "BBBBBBBBBB";

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    private static final String DEFAULT_DEVISE = "AAAAAAAAAA";
    private static final String UPDATED_DEVISE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CLOTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CLOTURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_PASSAGE_EXECUTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PASSAGE_EXECUTION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_LIEU_MULTIPLE_PAR_MISSION = false;
    private static final Boolean UPDATED_LIEU_MULTIPLE_PAR_MISSION = true;

    private static final Float DEFAULT_MONTANT_VENTE = 1F;
    private static final Float UPDATED_MONTANT_VENTE = 2F;

    private static final Float DEFAULT_MONTANT_BUDGETAIRE_MATERIEL = 1F;
    private static final Float UPDATED_MONTANT_BUDGETAIRE_MATERIEL = 2F;

    private static final Float DEFAULT_MONTANT_BUDGETAIRE_SERVICE = 1F;
    private static final Float UPDATED_MONTANT_BUDGETAIRE_SERVICE = 2F;

    private static final StatutAffaire DEFAULT_STATUT = StatutAffaire.Brouillon;
    private static final StatutAffaire UPDATED_STATUT = StatutAffaire.EtudeOpportunite;

    private static final String DEFAULT_RESPONSABLE_PROJET_ID = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE_PROJET_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE_PROJET_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE_PROJET_USER_LOGIN = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/affaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffaireRepository affaireRepository;

    @Mock
    private AffaireRepository affaireRepositoryMock;

    @Autowired
    private AffaireMapper affaireMapper;

    @Mock
    private AffaireService affaireServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffaireMockMvc;

    private Affaire affaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affaire createEntity(EntityManager em) {
        Affaire affaire = new Affaire()
            .numAffaire(DEFAULT_NUM_AFFAIRE)
            .designationAffaire(DEFAULT_DESIGNATION_AFFAIRE)
            .bonDeCommande(DEFAULT_BON_DE_COMMANDE)
            .montant(DEFAULT_MONTANT)
            .devise(DEFAULT_DEVISE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .datePassageExecution(DEFAULT_DATE_PASSAGE_EXECUTION)
            .lieuMultipleParMission(DEFAULT_LIEU_MULTIPLE_PAR_MISSION)
            .montantVente(DEFAULT_MONTANT_VENTE)
            .montantBudgetaireMateriel(DEFAULT_MONTANT_BUDGETAIRE_MATERIEL)
            .montantBudgetaireService(DEFAULT_MONTANT_BUDGETAIRE_SERVICE)
            .statut(DEFAULT_STATUT)
            .responsableProjetId(DEFAULT_RESPONSABLE_PROJET_ID)
            .responsableProjetUserLogin(DEFAULT_RESPONSABLE_PROJET_USER_LOGIN)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        affaire.setClient(client);
        return affaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Affaire createUpdatedEntity(EntityManager em) {
        Affaire affaire = new Affaire()
            .numAffaire(UPDATED_NUM_AFFAIRE)
            .designationAffaire(UPDATED_DESIGNATION_AFFAIRE)
            .bonDeCommande(UPDATED_BON_DE_COMMANDE)
            .montant(UPDATED_MONTANT)
            .devise(UPDATED_DEVISE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .datePassageExecution(UPDATED_DATE_PASSAGE_EXECUTION)
            .lieuMultipleParMission(UPDATED_LIEU_MULTIPLE_PAR_MISSION)
            .montantVente(UPDATED_MONTANT_VENTE)
            .montantBudgetaireMateriel(UPDATED_MONTANT_BUDGETAIRE_MATERIEL)
            .montantBudgetaireService(UPDATED_MONTANT_BUDGETAIRE_SERVICE)
            .statut(UPDATED_STATUT)
            .responsableProjetId(UPDATED_RESPONSABLE_PROJET_ID)
            .responsableProjetUserLogin(UPDATED_RESPONSABLE_PROJET_USER_LOGIN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        affaire.setClient(client);
        return affaire;
    }

    @BeforeEach
    public void initTest() {
        affaire = createEntity(em);
    }

    @Test
    @Transactional
    void createAffaire() throws Exception {
        int databaseSizeBeforeCreate = affaireRepository.findAll().size();
        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);
        restAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeCreate + 1);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getNumAffaire()).isEqualTo(DEFAULT_NUM_AFFAIRE);
        assertThat(testAffaire.getDesignationAffaire()).isEqualTo(DEFAULT_DESIGNATION_AFFAIRE);
        assertThat(testAffaire.getBonDeCommande()).isEqualTo(DEFAULT_BON_DE_COMMANDE);
        assertThat(testAffaire.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testAffaire.getDevise()).isEqualTo(DEFAULT_DEVISE);
        assertThat(testAffaire.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAffaire.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testAffaire.getDatePassageExecution()).isEqualTo(DEFAULT_DATE_PASSAGE_EXECUTION);
        assertThat(testAffaire.getLieuMultipleParMission()).isEqualTo(DEFAULT_LIEU_MULTIPLE_PAR_MISSION);
        assertThat(testAffaire.getMontantVente()).isEqualTo(DEFAULT_MONTANT_VENTE);
        assertThat(testAffaire.getMontantBudgetaireMateriel()).isEqualTo(DEFAULT_MONTANT_BUDGETAIRE_MATERIEL);
        assertThat(testAffaire.getMontantBudgetaireService()).isEqualTo(DEFAULT_MONTANT_BUDGETAIRE_SERVICE);
        assertThat(testAffaire.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAffaire.getResponsableProjetId()).isEqualTo(DEFAULT_RESPONSABLE_PROJET_ID);
        assertThat(testAffaire.getResponsableProjetUserLogin()).isEqualTo(DEFAULT_RESPONSABLE_PROJET_USER_LOGIN);
        assertThat(testAffaire.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAffaire.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testAffaire.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAffaire.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testAffaire.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testAffaire.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createAffaireWithExistingId() throws Exception {
        // Create the Affaire with an existing ID
        affaire.setId(1L);
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        int databaseSizeBeforeCreate = affaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumAffaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = affaireRepository.findAll().size();
        // set the field null
        affaire.setNumAffaire(null);

        // Create the Affaire, which fails.
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        restAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationAffaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = affaireRepository.findAll().size();
        // set the field null
        affaire.setDesignationAffaire(null);

        // Create the Affaire, which fails.
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        restAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = affaireRepository.findAll().size();
        // set the field null
        affaire.setStatut(null);

        // Create the Affaire, which fails.
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        restAffaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffaires() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        // Get all the affaireList
        restAffaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].numAffaire").value(hasItem(DEFAULT_NUM_AFFAIRE)))
            .andExpect(jsonPath("$.[*].designationAffaire").value(hasItem(DEFAULT_DESIGNATION_AFFAIRE)))
            .andExpect(jsonPath("$.[*].bonDeCommande").value(hasItem(DEFAULT_BON_DE_COMMANDE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].devise").value(hasItem(DEFAULT_DEVISE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].datePassageExecution").value(hasItem(DEFAULT_DATE_PASSAGE_EXECUTION.toString())))
            .andExpect(jsonPath("$.[*].lieuMultipleParMission").value(hasItem(DEFAULT_LIEU_MULTIPLE_PAR_MISSION.booleanValue())))
            .andExpect(jsonPath("$.[*].montantVente").value(hasItem(DEFAULT_MONTANT_VENTE.doubleValue())))
            .andExpect(jsonPath("$.[*].montantBudgetaireMateriel").value(hasItem(DEFAULT_MONTANT_BUDGETAIRE_MATERIEL.doubleValue())))
            .andExpect(jsonPath("$.[*].montantBudgetaireService").value(hasItem(DEFAULT_MONTANT_BUDGETAIRE_SERVICE.doubleValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].responsableProjetId").value(hasItem(DEFAULT_RESPONSABLE_PROJET_ID)))
            .andExpect(jsonPath("$.[*].responsableProjetUserLogin").value(hasItem(DEFAULT_RESPONSABLE_PROJET_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAffairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(affaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAffaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(affaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAffairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(affaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAffaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(affaireRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAffaire() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        // Get the affaire
        restAffaireMockMvc
            .perform(get(ENTITY_API_URL_ID, affaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affaire.getId().intValue()))
            .andExpect(jsonPath("$.numAffaire").value(DEFAULT_NUM_AFFAIRE))
            .andExpect(jsonPath("$.designationAffaire").value(DEFAULT_DESIGNATION_AFFAIRE))
            .andExpect(jsonPath("$.bonDeCommande").value(DEFAULT_BON_DE_COMMANDE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.devise").value(DEFAULT_DEVISE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateCloture").value(DEFAULT_DATE_CLOTURE.toString()))
            .andExpect(jsonPath("$.datePassageExecution").value(DEFAULT_DATE_PASSAGE_EXECUTION.toString()))
            .andExpect(jsonPath("$.lieuMultipleParMission").value(DEFAULT_LIEU_MULTIPLE_PAR_MISSION.booleanValue()))
            .andExpect(jsonPath("$.montantVente").value(DEFAULT_MONTANT_VENTE.doubleValue()))
            .andExpect(jsonPath("$.montantBudgetaireMateriel").value(DEFAULT_MONTANT_BUDGETAIRE_MATERIEL.doubleValue()))
            .andExpect(jsonPath("$.montantBudgetaireService").value(DEFAULT_MONTANT_BUDGETAIRE_SERVICE.doubleValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.responsableProjetId").value(DEFAULT_RESPONSABLE_PROJET_ID))
            .andExpect(jsonPath("$.responsableProjetUserLogin").value(DEFAULT_RESPONSABLE_PROJET_USER_LOGIN))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingAffaire() throws Exception {
        // Get the affaire
        restAffaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAffaire() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();

        // Update the affaire
        Affaire updatedAffaire = affaireRepository.findById(affaire.getId()).get();
        // Disconnect from session so that the updates on updatedAffaire are not directly saved in db
        em.detach(updatedAffaire);
        updatedAffaire
            .numAffaire(UPDATED_NUM_AFFAIRE)
            .designationAffaire(UPDATED_DESIGNATION_AFFAIRE)
            .bonDeCommande(UPDATED_BON_DE_COMMANDE)
            .montant(UPDATED_MONTANT)
            .devise(UPDATED_DEVISE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .datePassageExecution(UPDATED_DATE_PASSAGE_EXECUTION)
            .lieuMultipleParMission(UPDATED_LIEU_MULTIPLE_PAR_MISSION)
            .montantVente(UPDATED_MONTANT_VENTE)
            .montantBudgetaireMateriel(UPDATED_MONTANT_BUDGETAIRE_MATERIEL)
            .montantBudgetaireService(UPDATED_MONTANT_BUDGETAIRE_SERVICE)
            .statut(UPDATED_STATUT)
            .responsableProjetId(UPDATED_RESPONSABLE_PROJET_ID)
            .responsableProjetUserLogin(UPDATED_RESPONSABLE_PROJET_USER_LOGIN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        AffaireDTO affaireDTO = affaireMapper.toDto(updatedAffaire);

        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getNumAffaire()).isEqualTo(UPDATED_NUM_AFFAIRE);
        assertThat(testAffaire.getDesignationAffaire()).isEqualTo(UPDATED_DESIGNATION_AFFAIRE);
        assertThat(testAffaire.getBonDeCommande()).isEqualTo(UPDATED_BON_DE_COMMANDE);
        assertThat(testAffaire.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testAffaire.getDevise()).isEqualTo(UPDATED_DEVISE);
        assertThat(testAffaire.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAffaire.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testAffaire.getDatePassageExecution()).isEqualTo(UPDATED_DATE_PASSAGE_EXECUTION);
        assertThat(testAffaire.getLieuMultipleParMission()).isEqualTo(UPDATED_LIEU_MULTIPLE_PAR_MISSION);
        assertThat(testAffaire.getMontantVente()).isEqualTo(UPDATED_MONTANT_VENTE);
        assertThat(testAffaire.getMontantBudgetaireMateriel()).isEqualTo(UPDATED_MONTANT_BUDGETAIRE_MATERIEL);
        assertThat(testAffaire.getMontantBudgetaireService()).isEqualTo(UPDATED_MONTANT_BUDGETAIRE_SERVICE);
        assertThat(testAffaire.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAffaire.getResponsableProjetId()).isEqualTo(UPDATED_RESPONSABLE_PROJET_ID);
        assertThat(testAffaire.getResponsableProjetUserLogin()).isEqualTo(UPDATED_RESPONSABLE_PROJET_USER_LOGIN);
        assertThat(testAffaire.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAffaire.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAffaire.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAffaire.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testAffaire.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testAffaire.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffaireWithPatch() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();

        // Update the affaire using partial update
        Affaire partialUpdatedAffaire = new Affaire();
        partialUpdatedAffaire.setId(affaire.getId());

        partialUpdatedAffaire
            .numAffaire(UPDATED_NUM_AFFAIRE)
            .designationAffaire(UPDATED_DESIGNATION_AFFAIRE)
            .montant(UPDATED_MONTANT)
            .devise(UPDATED_DEVISE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .montantVente(UPDATED_MONTANT_VENTE)
            .montantBudgetaireMateriel(UPDATED_MONTANT_BUDGETAIRE_MATERIEL)
            .responsableProjetId(UPDATED_RESPONSABLE_PROJET_ID)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaire))
            )
            .andExpect(status().isOk());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getNumAffaire()).isEqualTo(UPDATED_NUM_AFFAIRE);
        assertThat(testAffaire.getDesignationAffaire()).isEqualTo(UPDATED_DESIGNATION_AFFAIRE);
        assertThat(testAffaire.getBonDeCommande()).isEqualTo(DEFAULT_BON_DE_COMMANDE);
        assertThat(testAffaire.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testAffaire.getDevise()).isEqualTo(UPDATED_DEVISE);
        assertThat(testAffaire.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testAffaire.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testAffaire.getDatePassageExecution()).isEqualTo(DEFAULT_DATE_PASSAGE_EXECUTION);
        assertThat(testAffaire.getLieuMultipleParMission()).isEqualTo(DEFAULT_LIEU_MULTIPLE_PAR_MISSION);
        assertThat(testAffaire.getMontantVente()).isEqualTo(UPDATED_MONTANT_VENTE);
        assertThat(testAffaire.getMontantBudgetaireMateriel()).isEqualTo(UPDATED_MONTANT_BUDGETAIRE_MATERIEL);
        assertThat(testAffaire.getMontantBudgetaireService()).isEqualTo(DEFAULT_MONTANT_BUDGETAIRE_SERVICE);
        assertThat(testAffaire.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testAffaire.getResponsableProjetId()).isEqualTo(UPDATED_RESPONSABLE_PROJET_ID);
        assertThat(testAffaire.getResponsableProjetUserLogin()).isEqualTo(DEFAULT_RESPONSABLE_PROJET_USER_LOGIN);
        assertThat(testAffaire.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAffaire.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAffaire.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAffaire.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testAffaire.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testAffaire.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateAffaireWithPatch() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();

        // Update the affaire using partial update
        Affaire partialUpdatedAffaire = new Affaire();
        partialUpdatedAffaire.setId(affaire.getId());

        partialUpdatedAffaire
            .numAffaire(UPDATED_NUM_AFFAIRE)
            .designationAffaire(UPDATED_DESIGNATION_AFFAIRE)
            .bonDeCommande(UPDATED_BON_DE_COMMANDE)
            .montant(UPDATED_MONTANT)
            .devise(UPDATED_DEVISE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .datePassageExecution(UPDATED_DATE_PASSAGE_EXECUTION)
            .lieuMultipleParMission(UPDATED_LIEU_MULTIPLE_PAR_MISSION)
            .montantVente(UPDATED_MONTANT_VENTE)
            .montantBudgetaireMateriel(UPDATED_MONTANT_BUDGETAIRE_MATERIEL)
            .montantBudgetaireService(UPDATED_MONTANT_BUDGETAIRE_SERVICE)
            .statut(UPDATED_STATUT)
            .responsableProjetId(UPDATED_RESPONSABLE_PROJET_ID)
            .responsableProjetUserLogin(UPDATED_RESPONSABLE_PROJET_USER_LOGIN)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaire))
            )
            .andExpect(status().isOk());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
        Affaire testAffaire = affaireList.get(affaireList.size() - 1);
        assertThat(testAffaire.getNumAffaire()).isEqualTo(UPDATED_NUM_AFFAIRE);
        assertThat(testAffaire.getDesignationAffaire()).isEqualTo(UPDATED_DESIGNATION_AFFAIRE);
        assertThat(testAffaire.getBonDeCommande()).isEqualTo(UPDATED_BON_DE_COMMANDE);
        assertThat(testAffaire.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testAffaire.getDevise()).isEqualTo(UPDATED_DEVISE);
        assertThat(testAffaire.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testAffaire.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testAffaire.getDatePassageExecution()).isEqualTo(UPDATED_DATE_PASSAGE_EXECUTION);
        assertThat(testAffaire.getLieuMultipleParMission()).isEqualTo(UPDATED_LIEU_MULTIPLE_PAR_MISSION);
        assertThat(testAffaire.getMontantVente()).isEqualTo(UPDATED_MONTANT_VENTE);
        assertThat(testAffaire.getMontantBudgetaireMateriel()).isEqualTo(UPDATED_MONTANT_BUDGETAIRE_MATERIEL);
        assertThat(testAffaire.getMontantBudgetaireService()).isEqualTo(UPDATED_MONTANT_BUDGETAIRE_SERVICE);
        assertThat(testAffaire.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testAffaire.getResponsableProjetId()).isEqualTo(UPDATED_RESPONSABLE_PROJET_ID);
        assertThat(testAffaire.getResponsableProjetUserLogin()).isEqualTo(UPDATED_RESPONSABLE_PROJET_USER_LOGIN);
        assertThat(testAffaire.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAffaire.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAffaire.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAffaire.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testAffaire.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testAffaire.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affaireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffaire() throws Exception {
        int databaseSizeBeforeUpdate = affaireRepository.findAll().size();
        affaire.setId(count.incrementAndGet());

        // Create the Affaire
        AffaireDTO affaireDTO = affaireMapper.toDto(affaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Affaire in the database
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffaire() throws Exception {
        // Initialize the database
        affaireRepository.saveAndFlush(affaire);

        int databaseSizeBeforeDelete = affaireRepository.findAll().size();

        // Delete the affaire
        restAffaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, affaire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Affaire> affaireList = affaireRepository.findAll();
        assertThat(affaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
