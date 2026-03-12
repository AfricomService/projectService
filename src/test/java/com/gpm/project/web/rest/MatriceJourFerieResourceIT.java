package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.JourFerie;
import com.gpm.project.domain.MatriceFacturation;
import com.gpm.project.domain.MatriceJourFerie;
import com.gpm.project.repository.MatriceJourFerieRepository;
import com.gpm.project.service.MatriceJourFerieService;
import com.gpm.project.service.dto.MatriceJourFerieDTO;
import com.gpm.project.service.mapper.MatriceJourFerieMapper;
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
 * Integration tests for the {@link MatriceJourFerieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MatriceJourFerieResourceIT {

    private static final Float DEFAULT_TARIF_APPLIQUE = 1F;
    private static final Float UPDATED_TARIF_APPLIQUE = 2F;

    private static final String ENTITY_API_URL = "/api/matrice-jour-feries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatriceJourFerieRepository matriceJourFerieRepository;

    @Mock
    private MatriceJourFerieRepository matriceJourFerieRepositoryMock;

    @Autowired
    private MatriceJourFerieMapper matriceJourFerieMapper;

    @Mock
    private MatriceJourFerieService matriceJourFerieServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatriceJourFerieMockMvc;

    private MatriceJourFerie matriceJourFerie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatriceJourFerie createEntity(EntityManager em) {
        MatriceJourFerie matriceJourFerie = new MatriceJourFerie().tarifApplique(DEFAULT_TARIF_APPLIQUE);
        // Add required entity
        MatriceFacturation matriceFacturation;
        if (TestUtil.findAll(em, MatriceFacturation.class).isEmpty()) {
            matriceFacturation = MatriceFacturationResourceIT.createEntity(em);
            em.persist(matriceFacturation);
            em.flush();
        } else {
            matriceFacturation = TestUtil.findAll(em, MatriceFacturation.class).get(0);
        }
        matriceJourFerie.setMatrice(matriceFacturation);
        // Add required entity
        JourFerie jourFerie;
        if (TestUtil.findAll(em, JourFerie.class).isEmpty()) {
            jourFerie = JourFerieResourceIT.createEntity(em);
            em.persist(jourFerie);
            em.flush();
        } else {
            jourFerie = TestUtil.findAll(em, JourFerie.class).get(0);
        }
        matriceJourFerie.setJourFerie(jourFerie);
        return matriceJourFerie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatriceJourFerie createUpdatedEntity(EntityManager em) {
        MatriceJourFerie matriceJourFerie = new MatriceJourFerie().tarifApplique(UPDATED_TARIF_APPLIQUE);
        // Add required entity
        MatriceFacturation matriceFacturation;
        if (TestUtil.findAll(em, MatriceFacturation.class).isEmpty()) {
            matriceFacturation = MatriceFacturationResourceIT.createUpdatedEntity(em);
            em.persist(matriceFacturation);
            em.flush();
        } else {
            matriceFacturation = TestUtil.findAll(em, MatriceFacturation.class).get(0);
        }
        matriceJourFerie.setMatrice(matriceFacturation);
        // Add required entity
        JourFerie jourFerie;
        if (TestUtil.findAll(em, JourFerie.class).isEmpty()) {
            jourFerie = JourFerieResourceIT.createUpdatedEntity(em);
            em.persist(jourFerie);
            em.flush();
        } else {
            jourFerie = TestUtil.findAll(em, JourFerie.class).get(0);
        }
        matriceJourFerie.setJourFerie(jourFerie);
        return matriceJourFerie;
    }

    @BeforeEach
    public void initTest() {
        matriceJourFerie = createEntity(em);
    }

    @Test
    @Transactional
    void createMatriceJourFerie() throws Exception {
        int databaseSizeBeforeCreate = matriceJourFerieRepository.findAll().size();
        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);
        restMatriceJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeCreate + 1);
        MatriceJourFerie testMatriceJourFerie = matriceJourFerieList.get(matriceJourFerieList.size() - 1);
        assertThat(testMatriceJourFerie.getTarifApplique()).isEqualTo(DEFAULT_TARIF_APPLIQUE);
    }

    @Test
    @Transactional
    void createMatriceJourFerieWithExistingId() throws Exception {
        // Create the MatriceJourFerie with an existing ID
        matriceJourFerie.setId(1L);
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        int databaseSizeBeforeCreate = matriceJourFerieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatriceJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTarifAppliqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = matriceJourFerieRepository.findAll().size();
        // set the field null
        matriceJourFerie.setTarifApplique(null);

        // Create the MatriceJourFerie, which fails.
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        restMatriceJourFerieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatriceJourFeries() throws Exception {
        // Initialize the database
        matriceJourFerieRepository.saveAndFlush(matriceJourFerie);

        // Get all the matriceJourFerieList
        restMatriceJourFerieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matriceJourFerie.getId().intValue())))
            .andExpect(jsonPath("$.[*].tarifApplique").value(hasItem(DEFAULT_TARIF_APPLIQUE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriceJourFeriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(matriceJourFerieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriceJourFerieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(matriceJourFerieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMatriceJourFeriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(matriceJourFerieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMatriceJourFerieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(matriceJourFerieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMatriceJourFerie() throws Exception {
        // Initialize the database
        matriceJourFerieRepository.saveAndFlush(matriceJourFerie);

        // Get the matriceJourFerie
        restMatriceJourFerieMockMvc
            .perform(get(ENTITY_API_URL_ID, matriceJourFerie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matriceJourFerie.getId().intValue()))
            .andExpect(jsonPath("$.tarifApplique").value(DEFAULT_TARIF_APPLIQUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMatriceJourFerie() throws Exception {
        // Get the matriceJourFerie
        restMatriceJourFerieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatriceJourFerie() throws Exception {
        // Initialize the database
        matriceJourFerieRepository.saveAndFlush(matriceJourFerie);

        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();

        // Update the matriceJourFerie
        MatriceJourFerie updatedMatriceJourFerie = matriceJourFerieRepository.findById(matriceJourFerie.getId()).get();
        // Disconnect from session so that the updates on updatedMatriceJourFerie are not directly saved in db
        em.detach(updatedMatriceJourFerie);
        updatedMatriceJourFerie.tarifApplique(UPDATED_TARIF_APPLIQUE);
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(updatedMatriceJourFerie);

        restMatriceJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriceJourFerieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isOk());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
        MatriceJourFerie testMatriceJourFerie = matriceJourFerieList.get(matriceJourFerieList.size() - 1);
        assertThat(testMatriceJourFerie.getTarifApplique()).isEqualTo(UPDATED_TARIF_APPLIQUE);
    }

    @Test
    @Transactional
    void putNonExistingMatriceJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();
        matriceJourFerie.setId(count.incrementAndGet());

        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriceJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matriceJourFerieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatriceJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();
        matriceJourFerie.setId(count.incrementAndGet());

        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatriceJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();
        matriceJourFerie.setId(count.incrementAndGet());

        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceJourFerieMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatriceJourFerieWithPatch() throws Exception {
        // Initialize the database
        matriceJourFerieRepository.saveAndFlush(matriceJourFerie);

        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();

        // Update the matriceJourFerie using partial update
        MatriceJourFerie partialUpdatedMatriceJourFerie = new MatriceJourFerie();
        partialUpdatedMatriceJourFerie.setId(matriceJourFerie.getId());

        partialUpdatedMatriceJourFerie.tarifApplique(UPDATED_TARIF_APPLIQUE);

        restMatriceJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatriceJourFerie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatriceJourFerie))
            )
            .andExpect(status().isOk());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
        MatriceJourFerie testMatriceJourFerie = matriceJourFerieList.get(matriceJourFerieList.size() - 1);
        assertThat(testMatriceJourFerie.getTarifApplique()).isEqualTo(UPDATED_TARIF_APPLIQUE);
    }

    @Test
    @Transactional
    void fullUpdateMatriceJourFerieWithPatch() throws Exception {
        // Initialize the database
        matriceJourFerieRepository.saveAndFlush(matriceJourFerie);

        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();

        // Update the matriceJourFerie using partial update
        MatriceJourFerie partialUpdatedMatriceJourFerie = new MatriceJourFerie();
        partialUpdatedMatriceJourFerie.setId(matriceJourFerie.getId());

        partialUpdatedMatriceJourFerie.tarifApplique(UPDATED_TARIF_APPLIQUE);

        restMatriceJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatriceJourFerie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatriceJourFerie))
            )
            .andExpect(status().isOk());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
        MatriceJourFerie testMatriceJourFerie = matriceJourFerieList.get(matriceJourFerieList.size() - 1);
        assertThat(testMatriceJourFerie.getTarifApplique()).isEqualTo(UPDATED_TARIF_APPLIQUE);
    }

    @Test
    @Transactional
    void patchNonExistingMatriceJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();
        matriceJourFerie.setId(count.incrementAndGet());

        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatriceJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matriceJourFerieDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatriceJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();
        matriceJourFerie.setId(count.incrementAndGet());

        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatriceJourFerie() throws Exception {
        int databaseSizeBeforeUpdate = matriceJourFerieRepository.findAll().size();
        matriceJourFerie.setId(count.incrementAndGet());

        // Create the MatriceJourFerie
        MatriceJourFerieDTO matriceJourFerieDTO = matriceJourFerieMapper.toDto(matriceJourFerie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatriceJourFerieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matriceJourFerieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MatriceJourFerie in the database
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatriceJourFerie() throws Exception {
        // Initialize the database
        matriceJourFerieRepository.saveAndFlush(matriceJourFerie);

        int databaseSizeBeforeDelete = matriceJourFerieRepository.findAll().size();

        // Delete the matriceJourFerie
        restMatriceJourFerieMockMvc
            .perform(delete(ENTITY_API_URL_ID, matriceJourFerie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatriceJourFerie> matriceJourFerieList = matriceJourFerieRepository.findAll();
        assertThat(matriceJourFerieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
