package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.TypeRessource;
import com.gpm.project.repository.TypeRessourceRepository;
import com.gpm.project.service.dto.TypeRessourceDTO;
import com.gpm.project.service.mapper.TypeRessourceMapper;
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
 * Integration tests for the {@link TypeRessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeRessourceResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeRessourceRepository typeRessourceRepository;

    @Autowired
    private TypeRessourceMapper typeRessourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeRessourceMockMvc;

    private TypeRessource typeRessource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRessource createEntity(EntityManager em) {
        TypeRessource typeRessource = new TypeRessource().type(DEFAULT_TYPE).code(DEFAULT_CODE);
        return typeRessource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRessource createUpdatedEntity(EntityManager em) {
        TypeRessource typeRessource = new TypeRessource().type(UPDATED_TYPE).code(UPDATED_CODE);
        return typeRessource;
    }

    @BeforeEach
    public void initTest() {
        typeRessource = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeRessource() throws Exception {
        int databaseSizeBeforeCreate = typeRessourceRepository.findAll().size();
        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);
        restTypeRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeCreate + 1);
        TypeRessource testTypeRessource = typeRessourceList.get(typeRessourceList.size() - 1);
        assertThat(testTypeRessource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTypeRessource.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createTypeRessourceWithExistingId() throws Exception {
        // Create the TypeRessource with an existing ID
        typeRessource.setId(1L);
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        int databaseSizeBeforeCreate = typeRessourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeRessources() throws Exception {
        // Initialize the database
        typeRessourceRepository.saveAndFlush(typeRessource);

        // Get all the typeRessourceList
        restTypeRessourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeRessource.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getTypeRessource() throws Exception {
        // Initialize the database
        typeRessourceRepository.saveAndFlush(typeRessource);

        // Get the typeRessource
        restTypeRessourceMockMvc
            .perform(get(ENTITY_API_URL_ID, typeRessource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeRessource.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingTypeRessource() throws Exception {
        // Get the typeRessource
        restTypeRessourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeRessource() throws Exception {
        // Initialize the database
        typeRessourceRepository.saveAndFlush(typeRessource);

        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();

        // Update the typeRessource
        TypeRessource updatedTypeRessource = typeRessourceRepository.findById(typeRessource.getId()).get();
        // Disconnect from session so that the updates on updatedTypeRessource are not directly saved in db
        em.detach(updatedTypeRessource);
        updatedTypeRessource.type(UPDATED_TYPE).code(UPDATED_CODE);
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(updatedTypeRessource);

        restTypeRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRessourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
        TypeRessource testTypeRessource = typeRessourceList.get(typeRessourceList.size() - 1);
        assertThat(testTypeRessource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypeRessource.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingTypeRessource() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();
        typeRessource.setId(count.incrementAndGet());

        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRessourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeRessource() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();
        typeRessource.setId(count.incrementAndGet());

        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeRessource() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();
        typeRessource.setId(count.incrementAndGet());

        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeRessourceWithPatch() throws Exception {
        // Initialize the database
        typeRessourceRepository.saveAndFlush(typeRessource);

        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();

        // Update the typeRessource using partial update
        TypeRessource partialUpdatedTypeRessource = new TypeRessource();
        partialUpdatedTypeRessource.setId(typeRessource.getId());

        partialUpdatedTypeRessource.code(UPDATED_CODE);

        restTypeRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRessource))
            )
            .andExpect(status().isOk());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
        TypeRessource testTypeRessource = typeRessourceList.get(typeRessourceList.size() - 1);
        assertThat(testTypeRessource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTypeRessource.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateTypeRessourceWithPatch() throws Exception {
        // Initialize the database
        typeRessourceRepository.saveAndFlush(typeRessource);

        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();

        // Update the typeRessource using partial update
        TypeRessource partialUpdatedTypeRessource = new TypeRessource();
        partialUpdatedTypeRessource.setId(typeRessource.getId());

        partialUpdatedTypeRessource.type(UPDATED_TYPE).code(UPDATED_CODE);

        restTypeRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRessource))
            )
            .andExpect(status().isOk());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
        TypeRessource testTypeRessource = typeRessourceList.get(typeRessourceList.size() - 1);
        assertThat(testTypeRessource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypeRessource.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingTypeRessource() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();
        typeRessource.setId(count.incrementAndGet());

        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeRessourceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeRessource() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();
        typeRessource.setId(count.incrementAndGet());

        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeRessource() throws Exception {
        int databaseSizeBeforeUpdate = typeRessourceRepository.findAll().size();
        typeRessource.setId(count.incrementAndGet());

        // Create the TypeRessource
        TypeRessourceDTO typeRessourceDTO = typeRessourceMapper.toDto(typeRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRessourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRessource in the database
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeRessource() throws Exception {
        // Initialize the database
        typeRessourceRepository.saveAndFlush(typeRessource);

        int databaseSizeBeforeDelete = typeRessourceRepository.findAll().size();

        // Delete the typeRessource
        restTypeRessourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeRessource.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeRessource> typeRessourceList = typeRessourceRepository.findAll();
        assertThat(typeRessourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
