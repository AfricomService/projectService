package com.gpm.project.web.rest;

import static com.gpm.project.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Societe;
import com.gpm.project.repository.SocieteRepository;
import com.gpm.project.service.dto.SocieteDTO;
import com.gpm.project.service.mapper.SocieteMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SocieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocieteResourceIT {

    private static final String DEFAULT_RAISON_SOCIALE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_SOCIALE_ABREGE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE_ABREGE = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFIANT_UNIQUE = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIANT_UNIQUE = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRE_COMMERCE = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRE_COMMERCE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_ARTICLE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ARTICLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE_POSTAL = 1;
    private static final Integer UPDATED_CODE_POSTAL = 2;

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/societes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocieteRepository societeRepository;

    @Autowired
    private SocieteMapper societeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocieteMockMvc;

    private Societe societe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Societe createEntity(EntityManager em) {
        Societe societe = new Societe()
            .raisonSociale(DEFAULT_RAISON_SOCIALE)
            .raisonSocialeAbrege(DEFAULT_RAISON_SOCIALE_ABREGE)
            .identifiantUnique(DEFAULT_IDENTIFIANT_UNIQUE)
            .registreCommerce(DEFAULT_REGISTRE_COMMERCE)
            .codeArticle(DEFAULT_CODE_ARTICLE)
            .adresse(DEFAULT_ADRESSE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .pays(DEFAULT_PAYS)
            .telephone(DEFAULT_TELEPHONE)
            .fax(DEFAULT_FAX)
            .email(DEFAULT_EMAIL)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdByUserLogin(DEFAULT_CREATED_BY_USER_LOGIN)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedByUserLogin(DEFAULT_UPDATED_BY_USER_LOGIN);
        return societe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Societe createUpdatedEntity(EntityManager em) {
        Societe societe = new Societe()
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .raisonSocialeAbrege(UPDATED_RAISON_SOCIALE_ABREGE)
            .identifiantUnique(UPDATED_IDENTIFIANT_UNIQUE)
            .registreCommerce(UPDATED_REGISTRE_COMMERCE)
            .codeArticle(UPDATED_CODE_ARTICLE)
            .adresse(UPDATED_ADRESSE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        return societe;
    }

    @BeforeEach
    public void initTest() {
        societe = createEntity(em);
    }

    @Test
    @Transactional
    void createSociete() throws Exception {
        int databaseSizeBeforeCreate = societeRepository.findAll().size();
        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);
        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeCreate + 1);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(DEFAULT_RAISON_SOCIALE);
        assertThat(testSociete.getRaisonSocialeAbrege()).isEqualTo(DEFAULT_RAISON_SOCIALE_ABREGE);
        assertThat(testSociete.getIdentifiantUnique()).isEqualTo(DEFAULT_IDENTIFIANT_UNIQUE);
        assertThat(testSociete.getRegistreCommerce()).isEqualTo(DEFAULT_REGISTRE_COMMERCE);
        assertThat(testSociete.getCodeArticle()).isEqualTo(DEFAULT_CODE_ARTICLE);
        assertThat(testSociete.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testSociete.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testSociete.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testSociete.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSociete.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testSociete.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSociete.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSociete.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testSociete.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSociete.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testSociete.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testSociete.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void createSocieteWithExistingId() throws Exception {
        // Create the Societe with an existing ID
        societe.setId(1L);
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        int databaseSizeBeforeCreate = societeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRaisonSocialeIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setRaisonSociale(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifiantUniqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setIdentifiantUnique(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setAdresse(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = societeRepository.findAll().size();
        // set the field null
        societe.setPays(null);

        // Create the Societe, which fails.
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        restSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocietes() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        // Get all the societeList
        restSocieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(societe.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE)))
            .andExpect(jsonPath("$.[*].raisonSocialeAbrege").value(hasItem(DEFAULT_RAISON_SOCIALE_ABREGE)))
            .andExpect(jsonPath("$.[*].identifiantUnique").value(hasItem(DEFAULT_IDENTIFIANT_UNIQUE)))
            .andExpect(jsonPath("$.[*].registreCommerce").value(hasItem(DEFAULT_REGISTRE_COMMERCE)))
            .andExpect(jsonPath("$.[*].codeArticle").value(hasItem(DEFAULT_CODE_ARTICLE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdByUserLogin").value(hasItem(DEFAULT_CREATED_BY_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedByUserLogin").value(hasItem(DEFAULT_UPDATED_BY_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getSociete() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        // Get the societe
        restSocieteMockMvc
            .perform(get(ENTITY_API_URL_ID, societe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(societe.getId().intValue()))
            .andExpect(jsonPath("$.raisonSociale").value(DEFAULT_RAISON_SOCIALE))
            .andExpect(jsonPath("$.raisonSocialeAbrege").value(DEFAULT_RAISON_SOCIALE_ABREGE))
            .andExpect(jsonPath("$.identifiantUnique").value(DEFAULT_IDENTIFIANT_UNIQUE))
            .andExpect(jsonPath("$.registreCommerce").value(DEFAULT_REGISTRE_COMMERCE))
            .andExpect(jsonPath("$.codeArticle").value(DEFAULT_CODE_ARTICLE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdByUserLogin").value(DEFAULT_CREATED_BY_USER_LOGIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedByUserLogin").value(DEFAULT_UPDATED_BY_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingSociete() throws Exception {
        // Get the societe
        restSocieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSociete() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeUpdate = societeRepository.findAll().size();

        // Update the societe
        Societe updatedSociete = societeRepository.findById(societe.getId()).get();
        // Disconnect from session so that the updates on updatedSociete are not directly saved in db
        em.detach(updatedSociete);
        updatedSociete
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .raisonSocialeAbrege(UPDATED_RAISON_SOCIALE_ABREGE)
            .identifiantUnique(UPDATED_IDENTIFIANT_UNIQUE)
            .registreCommerce(UPDATED_REGISTRE_COMMERCE)
            .codeArticle(UPDATED_CODE_ARTICLE)
            .adresse(UPDATED_ADRESSE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);
        SocieteDTO societeDTO = societeMapper.toDto(updatedSociete);

        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, societeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testSociete.getRaisonSocialeAbrege()).isEqualTo(UPDATED_RAISON_SOCIALE_ABREGE);
        assertThat(testSociete.getIdentifiantUnique()).isEqualTo(UPDATED_IDENTIFIANT_UNIQUE);
        assertThat(testSociete.getRegistreCommerce()).isEqualTo(UPDATED_REGISTRE_COMMERCE);
        assertThat(testSociete.getCodeArticle()).isEqualTo(UPDATED_CODE_ARTICLE);
        assertThat(testSociete.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSociete.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testSociete.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testSociete.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSociete.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testSociete.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSociete.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSociete.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSociete.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSociete.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testSociete.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testSociete.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, societeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocieteWithPatch() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeUpdate = societeRepository.findAll().size();

        // Update the societe using partial update
        Societe partialUpdatedSociete = new Societe();
        partialUpdatedSociete.setId(societe.getId());

        partialUpdatedSociete
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .raisonSocialeAbrege(UPDATED_RAISON_SOCIALE_ABREGE)
            .identifiantUnique(UPDATED_IDENTIFIANT_UNIQUE)
            .codeArticle(UPDATED_CODE_ARTICLE)
            .adresse(UPDATED_ADRESSE)
            .email(UPDATED_EMAIL)
            .updatedAt(UPDATED_UPDATED_AT);

        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSociete))
            )
            .andExpect(status().isOk());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testSociete.getRaisonSocialeAbrege()).isEqualTo(UPDATED_RAISON_SOCIALE_ABREGE);
        assertThat(testSociete.getIdentifiantUnique()).isEqualTo(UPDATED_IDENTIFIANT_UNIQUE);
        assertThat(testSociete.getRegistreCommerce()).isEqualTo(DEFAULT_REGISTRE_COMMERCE);
        assertThat(testSociete.getCodeArticle()).isEqualTo(UPDATED_CODE_ARTICLE);
        assertThat(testSociete.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSociete.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testSociete.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testSociete.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSociete.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testSociete.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSociete.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testSociete.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSociete.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSociete.getCreatedByUserLogin()).isEqualTo(DEFAULT_CREATED_BY_USER_LOGIN);
        assertThat(testSociete.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testSociete.getUpdatedByUserLogin()).isEqualTo(DEFAULT_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateSocieteWithPatch() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeUpdate = societeRepository.findAll().size();

        // Update the societe using partial update
        Societe partialUpdatedSociete = new Societe();
        partialUpdatedSociete.setId(societe.getId());

        partialUpdatedSociete
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .raisonSocialeAbrege(UPDATED_RAISON_SOCIALE_ABREGE)
            .identifiantUnique(UPDATED_IDENTIFIANT_UNIQUE)
            .registreCommerce(UPDATED_REGISTRE_COMMERCE)
            .codeArticle(UPDATED_CODE_ARTICLE)
            .adresse(UPDATED_ADRESSE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE)
            .fax(UPDATED_FAX)
            .email(UPDATED_EMAIL)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdByUserLogin(UPDATED_CREATED_BY_USER_LOGIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedByUserLogin(UPDATED_UPDATED_BY_USER_LOGIN);

        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSociete))
            )
            .andExpect(status().isOk());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
        Societe testSociete = societeList.get(societeList.size() - 1);
        assertThat(testSociete.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testSociete.getRaisonSocialeAbrege()).isEqualTo(UPDATED_RAISON_SOCIALE_ABREGE);
        assertThat(testSociete.getIdentifiantUnique()).isEqualTo(UPDATED_IDENTIFIANT_UNIQUE);
        assertThat(testSociete.getRegistreCommerce()).isEqualTo(UPDATED_REGISTRE_COMMERCE);
        assertThat(testSociete.getCodeArticle()).isEqualTo(UPDATED_CODE_ARTICLE);
        assertThat(testSociete.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSociete.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testSociete.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testSociete.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSociete.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testSociete.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSociete.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testSociete.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testSociete.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSociete.getCreatedByUserLogin()).isEqualTo(UPDATED_CREATED_BY_USER_LOGIN);
        assertThat(testSociete.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testSociete.getUpdatedByUserLogin()).isEqualTo(UPDATED_UPDATED_BY_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, societeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSociete() throws Exception {
        int databaseSizeBeforeUpdate = societeRepository.findAll().size();
        societe.setId(count.incrementAndGet());

        // Create the Societe
        SocieteDTO societeDTO = societeMapper.toDto(societe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(societeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Societe in the database
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSociete() throws Exception {
        // Initialize the database
        societeRepository.saveAndFlush(societe);

        int databaseSizeBeforeDelete = societeRepository.findAll().size();

        // Delete the societe
        restSocieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, societe.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Societe> societeList = societeRepository.findAll();
        assertThat(societeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
