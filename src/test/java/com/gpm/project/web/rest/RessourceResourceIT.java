package com.gpm.project.web.rest;

import static com.gpm.project.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Ressource;
import com.gpm.project.repository.RessourceRepository;
import com.gpm.project.service.dto.RessourceDTO;
import com.gpm.project.service.mapper.RessourceMapper;
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
 * Integration tests for the {@link RessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RessourceResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_MISE_EN_SERVICE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_MISE_EN_SERVICE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_DERNIERE_MAINTENANCE = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATE_DERNIERE_MAINTENANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_PROCHAINE_MAINTENANCE = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_DATE_PROCHAINE_MAINTENANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_TYPE_RESSOURCE_ID = 1L;
    private static final Long UPDATED_TYPE_RESSOURCE_ID = 2L;

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RessourceRepository ressourceRepository;

    @Autowired
    private RessourceMapper ressourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRessourceMockMvc;

    private Ressource ressource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressource createEntity(EntityManager em) {
        Ressource ressource = new Ressource()
            .nom(DEFAULT_NOM)
            .code(DEFAULT_CODE)
            .categorie(DEFAULT_CATEGORIE)
            .description(DEFAULT_DESCRIPTION)
            .dateMiseEnService(DEFAULT_DATE_MISE_EN_SERVICE)
            .dateDerniereMaintenance(DEFAULT_DATE_DERNIERE_MAINTENANCE)
            .dateProchaineMaintenance(DEFAULT_DATE_PROCHAINE_MAINTENANCE)
            .typeRessourceId(DEFAULT_TYPE_RESSOURCE_ID)
            .statut(DEFAULT_STATUT);
        return ressource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressource createUpdatedEntity(EntityManager em) {
        Ressource ressource = new Ressource()
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .categorie(UPDATED_CATEGORIE)
            .description(UPDATED_DESCRIPTION)
            .dateMiseEnService(UPDATED_DATE_MISE_EN_SERVICE)
            .dateDerniereMaintenance(UPDATED_DATE_DERNIERE_MAINTENANCE)
            .dateProchaineMaintenance(UPDATED_DATE_PROCHAINE_MAINTENANCE)
            .typeRessourceId(UPDATED_TYPE_RESSOURCE_ID)
            .statut(UPDATED_STATUT);
        return ressource;
    }

    @BeforeEach
    public void initTest() {
        ressource = createEntity(em);
    }

    @Test
    @Transactional
    void createRessource() throws Exception {
        int databaseSizeBeforeCreate = ressourceRepository.findAll().size();
        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);
        restRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeCreate + 1);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRessource.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRessource.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testRessource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRessource.getDateMiseEnService()).isEqualTo(DEFAULT_DATE_MISE_EN_SERVICE);
        assertThat(testRessource.getDateDerniereMaintenance()).isEqualTo(DEFAULT_DATE_DERNIERE_MAINTENANCE);
        assertThat(testRessource.getDateProchaineMaintenance()).isEqualTo(DEFAULT_DATE_PROCHAINE_MAINTENANCE);
        assertThat(testRessource.getTypeRessourceId()).isEqualTo(DEFAULT_TYPE_RESSOURCE_ID);
        assertThat(testRessource.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void createRessourceWithExistingId() throws Exception {
        // Create the Ressource with an existing ID
        ressource.setId(1L);
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        int databaseSizeBeforeCreate = ressourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRessources() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        // Get all the ressourceList
        restRessourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressource.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateMiseEnService").value(hasItem(sameInstant(DEFAULT_DATE_MISE_EN_SERVICE))))
            .andExpect(jsonPath("$.[*].dateDerniereMaintenance").value(hasItem(sameInstant(DEFAULT_DATE_DERNIERE_MAINTENANCE))))
            .andExpect(jsonPath("$.[*].dateProchaineMaintenance").value(hasItem(sameInstant(DEFAULT_DATE_PROCHAINE_MAINTENANCE))))
            .andExpect(jsonPath("$.[*].typeRessourceId").value(hasItem(DEFAULT_TYPE_RESSOURCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)));
    }

    @Test
    @Transactional
    void getRessource() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        // Get the ressource
        restRessourceMockMvc
            .perform(get(ENTITY_API_URL_ID, ressource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ressource.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateMiseEnService").value(sameInstant(DEFAULT_DATE_MISE_EN_SERVICE)))
            .andExpect(jsonPath("$.dateDerniereMaintenance").value(sameInstant(DEFAULT_DATE_DERNIERE_MAINTENANCE)))
            .andExpect(jsonPath("$.dateProchaineMaintenance").value(sameInstant(DEFAULT_DATE_PROCHAINE_MAINTENANCE)))
            .andExpect(jsonPath("$.typeRessourceId").value(DEFAULT_TYPE_RESSOURCE_ID.intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT));
    }

    @Test
    @Transactional
    void getNonExistingRessource() throws Exception {
        // Get the ressource
        restRessourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRessource() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();

        // Update the ressource
        Ressource updatedRessource = ressourceRepository.findById(ressource.getId()).get();
        // Disconnect from session so that the updates on updatedRessource are not directly saved in db
        em.detach(updatedRessource);
        updatedRessource
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .categorie(UPDATED_CATEGORIE)
            .description(UPDATED_DESCRIPTION)
            .dateMiseEnService(UPDATED_DATE_MISE_EN_SERVICE)
            .dateDerniereMaintenance(UPDATED_DATE_DERNIERE_MAINTENANCE)
            .dateProchaineMaintenance(UPDATED_DATE_PROCHAINE_MAINTENANCE)
            .typeRessourceId(UPDATED_TYPE_RESSOURCE_ID)
            .statut(UPDATED_STATUT);
        RessourceDTO ressourceDTO = ressourceMapper.toDto(updatedRessource);

        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ressourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRessource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRessource.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testRessource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRessource.getDateMiseEnService()).isEqualTo(UPDATED_DATE_MISE_EN_SERVICE);
        assertThat(testRessource.getDateDerniereMaintenance()).isEqualTo(UPDATED_DATE_DERNIERE_MAINTENANCE);
        assertThat(testRessource.getDateProchaineMaintenance()).isEqualTo(UPDATED_DATE_PROCHAINE_MAINTENANCE);
        assertThat(testRessource.getTypeRessourceId()).isEqualTo(UPDATED_TYPE_RESSOURCE_ID);
        assertThat(testRessource.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void putNonExistingRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ressourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRessourceWithPatch() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();

        // Update the ressource using partial update
        Ressource partialUpdatedRessource = new Ressource();
        partialUpdatedRessource.setId(ressource.getId());

        partialUpdatedRessource.description(UPDATED_DESCRIPTION).dateProchaineMaintenance(UPDATED_DATE_PROCHAINE_MAINTENANCE);

        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRessource))
            )
            .andExpect(status().isOk());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRessource.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRessource.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testRessource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRessource.getDateMiseEnService()).isEqualTo(DEFAULT_DATE_MISE_EN_SERVICE);
        assertThat(testRessource.getDateDerniereMaintenance()).isEqualTo(DEFAULT_DATE_DERNIERE_MAINTENANCE);
        assertThat(testRessource.getDateProchaineMaintenance()).isEqualTo(UPDATED_DATE_PROCHAINE_MAINTENANCE);
        assertThat(testRessource.getTypeRessourceId()).isEqualTo(DEFAULT_TYPE_RESSOURCE_ID);
        assertThat(testRessource.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void fullUpdateRessourceWithPatch() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();

        // Update the ressource using partial update
        Ressource partialUpdatedRessource = new Ressource();
        partialUpdatedRessource.setId(ressource.getId());

        partialUpdatedRessource
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .categorie(UPDATED_CATEGORIE)
            .description(UPDATED_DESCRIPTION)
            .dateMiseEnService(UPDATED_DATE_MISE_EN_SERVICE)
            .dateDerniereMaintenance(UPDATED_DATE_DERNIERE_MAINTENANCE)
            .dateProchaineMaintenance(UPDATED_DATE_PROCHAINE_MAINTENANCE)
            .typeRessourceId(UPDATED_TYPE_RESSOURCE_ID)
            .statut(UPDATED_STATUT);

        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRessource))
            )
            .andExpect(status().isOk());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
        Ressource testRessource = ressourceList.get(ressourceList.size() - 1);
        assertThat(testRessource.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRessource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRessource.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testRessource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRessource.getDateMiseEnService()).isEqualTo(UPDATED_DATE_MISE_EN_SERVICE);
        assertThat(testRessource.getDateDerniereMaintenance()).isEqualTo(UPDATED_DATE_DERNIERE_MAINTENANCE);
        assertThat(testRessource.getDateProchaineMaintenance()).isEqualTo(UPDATED_DATE_PROCHAINE_MAINTENANCE);
        assertThat(testRessource.getTypeRessourceId()).isEqualTo(UPDATED_TYPE_RESSOURCE_ID);
        assertThat(testRessource.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void patchNonExistingRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ressourceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRessource() throws Exception {
        int databaseSizeBeforeUpdate = ressourceRepository.findAll().size();
        ressource.setId(count.incrementAndGet());

        // Create the Ressource
        RessourceDTO ressourceDTO = ressourceMapper.toDto(ressource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ressourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ressource in the database
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRessource() throws Exception {
        // Initialize the database
        ressourceRepository.saveAndFlush(ressource);

        int databaseSizeBeforeDelete = ressourceRepository.findAll().size();

        // Delete the ressource
        restRessourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, ressource.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ressource> ressourceList = ressourceRepository.findAll();
        assertThat(ressourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
