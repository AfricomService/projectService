package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.TypeRessourceDetails;
import com.gpm.project.repository.TypeRessourceDetailsRepository;
import com.gpm.project.service.dto.TypeRessourceDetailsDTO;
import com.gpm.project.service.mapper.TypeRessourceDetailsMapper;
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
 * Integration tests for the {@link TypeRessourceDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeRessourceDetailsResourceIT {

    private static final Long DEFAULT_TYPE_RESSOURCE_ID = 1L;
    private static final Long UPDATED_TYPE_RESSOURCE_ID = 2L;

    private static final Long DEFAULT_DETAIL_RESSOURCE_ID = 1L;
    private static final Long UPDATED_DETAIL_RESSOURCE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/type-ressource-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeRessourceDetailsRepository typeRessourceDetailsRepository;

    @Autowired
    private TypeRessourceDetailsMapper typeRessourceDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeRessourceDetailsMockMvc;

    private TypeRessourceDetails typeRessourceDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRessourceDetails createEntity(EntityManager em) {
        TypeRessourceDetails typeRessourceDetails = new TypeRessourceDetails()
            .typeRessourceId(DEFAULT_TYPE_RESSOURCE_ID)
            .detailRessourceId(DEFAULT_DETAIL_RESSOURCE_ID);
        return typeRessourceDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRessourceDetails createUpdatedEntity(EntityManager em) {
        TypeRessourceDetails typeRessourceDetails = new TypeRessourceDetails()
            .typeRessourceId(UPDATED_TYPE_RESSOURCE_ID)
            .detailRessourceId(UPDATED_DETAIL_RESSOURCE_ID);
        return typeRessourceDetails;
    }

    @BeforeEach
    public void initTest() {
        typeRessourceDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeCreate = typeRessourceDetailsRepository.findAll().size();
        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);
        restTypeRessourceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TypeRessourceDetails testTypeRessourceDetails = typeRessourceDetailsList.get(typeRessourceDetailsList.size() - 1);
        assertThat(testTypeRessourceDetails.getTypeRessourceId()).isEqualTo(DEFAULT_TYPE_RESSOURCE_ID);
        assertThat(testTypeRessourceDetails.getDetailRessourceId()).isEqualTo(DEFAULT_DETAIL_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void createTypeRessourceDetailsWithExistingId() throws Exception {
        // Create the TypeRessourceDetails with an existing ID
        typeRessourceDetails.setId(1L);
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        int databaseSizeBeforeCreate = typeRessourceDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeRessourceDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeRessourceDetails() throws Exception {
        // Initialize the database
        typeRessourceDetailsRepository.saveAndFlush(typeRessourceDetails);

        // Get all the typeRessourceDetailsList
        restTypeRessourceDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeRessourceDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeRessourceId").value(hasItem(DEFAULT_TYPE_RESSOURCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].detailRessourceId").value(hasItem(DEFAULT_DETAIL_RESSOURCE_ID.intValue())));
    }

    @Test
    @Transactional
    void getTypeRessourceDetails() throws Exception {
        // Initialize the database
        typeRessourceDetailsRepository.saveAndFlush(typeRessourceDetails);

        // Get the typeRessourceDetails
        restTypeRessourceDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, typeRessourceDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeRessourceDetails.getId().intValue()))
            .andExpect(jsonPath("$.typeRessourceId").value(DEFAULT_TYPE_RESSOURCE_ID.intValue()))
            .andExpect(jsonPath("$.detailRessourceId").value(DEFAULT_DETAIL_RESSOURCE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTypeRessourceDetails() throws Exception {
        // Get the typeRessourceDetails
        restTypeRessourceDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeRessourceDetails() throws Exception {
        // Initialize the database
        typeRessourceDetailsRepository.saveAndFlush(typeRessourceDetails);

        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();

        // Update the typeRessourceDetails
        TypeRessourceDetails updatedTypeRessourceDetails = typeRessourceDetailsRepository.findById(typeRessourceDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTypeRessourceDetails are not directly saved in db
        em.detach(updatedTypeRessourceDetails);
        updatedTypeRessourceDetails.typeRessourceId(UPDATED_TYPE_RESSOURCE_ID).detailRessourceId(UPDATED_DETAIL_RESSOURCE_ID);
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(updatedTypeRessourceDetails);

        restTypeRessourceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRessourceDetailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
        TypeRessourceDetails testTypeRessourceDetails = typeRessourceDetailsList.get(typeRessourceDetailsList.size() - 1);
        assertThat(testTypeRessourceDetails.getTypeRessourceId()).isEqualTo(UPDATED_TYPE_RESSOURCE_ID);
        assertThat(testTypeRessourceDetails.getDetailRessourceId()).isEqualTo(UPDATED_DETAIL_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void putNonExistingTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();
        typeRessourceDetails.setId(count.incrementAndGet());

        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRessourceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRessourceDetailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();
        typeRessourceDetails.setId(count.incrementAndGet());

        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();
        typeRessourceDetails.setId(count.incrementAndGet());

        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeRessourceDetailsWithPatch() throws Exception {
        // Initialize the database
        typeRessourceDetailsRepository.saveAndFlush(typeRessourceDetails);

        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();

        // Update the typeRessourceDetails using partial update
        TypeRessourceDetails partialUpdatedTypeRessourceDetails = new TypeRessourceDetails();
        partialUpdatedTypeRessourceDetails.setId(typeRessourceDetails.getId());

        partialUpdatedTypeRessourceDetails.detailRessourceId(UPDATED_DETAIL_RESSOURCE_ID);

        restTypeRessourceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRessourceDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRessourceDetails))
            )
            .andExpect(status().isOk());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
        TypeRessourceDetails testTypeRessourceDetails = typeRessourceDetailsList.get(typeRessourceDetailsList.size() - 1);
        assertThat(testTypeRessourceDetails.getTypeRessourceId()).isEqualTo(DEFAULT_TYPE_RESSOURCE_ID);
        assertThat(testTypeRessourceDetails.getDetailRessourceId()).isEqualTo(UPDATED_DETAIL_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateTypeRessourceDetailsWithPatch() throws Exception {
        // Initialize the database
        typeRessourceDetailsRepository.saveAndFlush(typeRessourceDetails);

        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();

        // Update the typeRessourceDetails using partial update
        TypeRessourceDetails partialUpdatedTypeRessourceDetails = new TypeRessourceDetails();
        partialUpdatedTypeRessourceDetails.setId(typeRessourceDetails.getId());

        partialUpdatedTypeRessourceDetails.typeRessourceId(UPDATED_TYPE_RESSOURCE_ID).detailRessourceId(UPDATED_DETAIL_RESSOURCE_ID);

        restTypeRessourceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRessourceDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRessourceDetails))
            )
            .andExpect(status().isOk());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
        TypeRessourceDetails testTypeRessourceDetails = typeRessourceDetailsList.get(typeRessourceDetailsList.size() - 1);
        assertThat(testTypeRessourceDetails.getTypeRessourceId()).isEqualTo(UPDATED_TYPE_RESSOURCE_ID);
        assertThat(testTypeRessourceDetails.getDetailRessourceId()).isEqualTo(UPDATED_DETAIL_RESSOURCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();
        typeRessourceDetails.setId(count.incrementAndGet());

        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRessourceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeRessourceDetailsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();
        typeRessourceDetails.setId(count.incrementAndGet());

        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeRessourceDetails() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceDetailsRepository.findAll().size();
        typeRessourceDetails.setId(count.incrementAndGet());

        // Create the TypeRessourceDetails
        TypeRessourceDetailsDTO typeRessourceDetailsDTO = typeRessourceDetailsMapper.toDto(typeRessourceDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRessourceDetails in the database
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeRessourceDetails() throws Exception {
        // Initialize the database
        typeRessourceDetailsRepository.saveAndFlush(typeRessourceDetails);

        int databaseSizeBeforeDelete = typeRessourceDetailsRepository.findAll().size();

        // Delete the typeRessourceDetails
        restTypeRessourceDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeRessourceDetails.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeRessourceDetails> typeRessourceDetailsList = typeRessourceDetailsRepository.findAll();
        assertThat(typeRessourceDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
