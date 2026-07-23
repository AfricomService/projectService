package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.ParentClassdef;
import com.gpm.project.repository.ParentClassdefRepository;
import com.gpm.project.service.dto.ParentClassdefDTO;
import com.gpm.project.service.mapper.ParentClassdefMapper;
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
 * Integration tests for the {@link ParentClassdefResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParentClassdefResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FORMULE = "AAAAAAAAAA";
    private static final String UPDATED_FORMULE = "BBBBBBBBBB";

    private static final Long DEFAULT_SEQUENCE_ID = 1L;
    private static final Long UPDATED_SEQUENCE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/parent-classdefs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParentClassdefRepository parentClassdefRepository;

    @Autowired
    private ParentClassdefMapper parentClassdefMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParentClassdefMockMvc;

    private ParentClassdef parentClassdef;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParentClassdef createEntity(EntityManager em) {
        ParentClassdef parentClassdef = new ParentClassdef().type(DEFAULT_TYPE).formule(DEFAULT_FORMULE).sequenceId(DEFAULT_SEQUENCE_ID);
        return parentClassdef;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParentClassdef createUpdatedEntity(EntityManager em) {
        ParentClassdef parentClassdef = new ParentClassdef().type(UPDATED_TYPE).formule(UPDATED_FORMULE).sequenceId(UPDATED_SEQUENCE_ID);
        return parentClassdef;
    }

    @BeforeEach
    public void initTest() {
        parentClassdef = createEntity(em);
    }

    @Test
    @Transactional
    void createParentClassdef() throws Exception {
        int databaseSizeBeforeCreate = parentClassdefRepository.findAll().size();
        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);
        restParentClassdefMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeCreate + 1);
        ParentClassdef testParentClassdef = parentClassdefList.get(parentClassdefList.size() - 1);
        assertThat(testParentClassdef.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParentClassdef.getFormule()).isEqualTo(DEFAULT_FORMULE);
        assertThat(testParentClassdef.getSequenceId()).isEqualTo(DEFAULT_SEQUENCE_ID);
    }

    @Test
    @Transactional
    void createParentClassdefWithExistingId() throws Exception {
        // Create the ParentClassdef with an existing ID
        parentClassdef.setId(1L);
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        int databaseSizeBeforeCreate = parentClassdefRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParentClassdefMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParentClassdefs() throws Exception {
        // Initialize the database
        parentClassdefRepository.saveAndFlush(parentClassdef);

        // Get all the parentClassdefList
        restParentClassdefMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parentClassdef.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].formule").value(hasItem(DEFAULT_FORMULE)))
            .andExpect(jsonPath("$.[*].sequenceId").value(hasItem(DEFAULT_SEQUENCE_ID.intValue())));
    }

    @Test
    @Transactional
    void getParentClassdef() throws Exception {
        // Initialize the database
        parentClassdefRepository.saveAndFlush(parentClassdef);

        // Get the parentClassdef
        restParentClassdefMockMvc
            .perform(get(ENTITY_API_URL_ID, parentClassdef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parentClassdef.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.formule").value(DEFAULT_FORMULE))
            .andExpect(jsonPath("$.sequenceId").value(DEFAULT_SEQUENCE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingParentClassdef() throws Exception {
        // Get the parentClassdef
        restParentClassdefMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParentClassdef() throws Exception {
        // Initialize the database
        parentClassdefRepository.saveAndFlush(parentClassdef);

        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();

        // Update the parentClassdef
        ParentClassdef updatedParentClassdef = parentClassdefRepository.findById(parentClassdef.getId()).get();
        // Disconnect from session so that the updates on updatedParentClassdef are not directly saved in db
        em.detach(updatedParentClassdef);
        updatedParentClassdef.type(UPDATED_TYPE).formule(UPDATED_FORMULE).sequenceId(UPDATED_SEQUENCE_ID);
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(updatedParentClassdef);

        restParentClassdefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parentClassdefDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isOk());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
        ParentClassdef testParentClassdef = parentClassdefList.get(parentClassdefList.size() - 1);
        assertThat(testParentClassdef.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParentClassdef.getFormule()).isEqualTo(UPDATED_FORMULE);
        assertThat(testParentClassdef.getSequenceId()).isEqualTo(UPDATED_SEQUENCE_ID);
    }

    @Test
    @Transactional
    void putNonExistingParentClassdef() throws Exception {
        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();
        parentClassdef.setId(count.incrementAndGet());

        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParentClassdefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parentClassdefDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParentClassdef() throws Exception {
        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();
        parentClassdef.setId(count.incrementAndGet());

        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentClassdefMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParentClassdef() throws Exception {
        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();
        parentClassdef.setId(count.incrementAndGet());

        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentClassdefMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParentClassdefWithPatch() throws Exception {
        // Initialize the database
        parentClassdefRepository.saveAndFlush(parentClassdef);

        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();

        // Update the parentClassdef using partial update
        ParentClassdef partialUpdatedParentClassdef = new ParentClassdef();
        partialUpdatedParentClassdef.setId(parentClassdef.getId());

        partialUpdatedParentClassdef.type(UPDATED_TYPE);

        restParentClassdefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParentClassdef.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParentClassdef))
            )
            .andExpect(status().isOk());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
        ParentClassdef testParentClassdef = parentClassdefList.get(parentClassdefList.size() - 1);
        assertThat(testParentClassdef.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParentClassdef.getFormule()).isEqualTo(DEFAULT_FORMULE);
        assertThat(testParentClassdef.getSequenceId()).isEqualTo(DEFAULT_SEQUENCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateParentClassdefWithPatch() throws Exception {
        // Initialize the database
        parentClassdefRepository.saveAndFlush(parentClassdef);

        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();

        // Update the parentClassdef using partial update
        ParentClassdef partialUpdatedParentClassdef = new ParentClassdef();
        partialUpdatedParentClassdef.setId(parentClassdef.getId());

        partialUpdatedParentClassdef.type(UPDATED_TYPE).formule(UPDATED_FORMULE).sequenceId(UPDATED_SEQUENCE_ID);

        restParentClassdefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParentClassdef.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParentClassdef))
            )
            .andExpect(status().isOk());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
        ParentClassdef testParentClassdef = parentClassdefList.get(parentClassdefList.size() - 1);
        assertThat(testParentClassdef.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParentClassdef.getFormule()).isEqualTo(UPDATED_FORMULE);
        assertThat(testParentClassdef.getSequenceId()).isEqualTo(UPDATED_SEQUENCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingParentClassdef() throws Exception {
        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();
        parentClassdef.setId(count.incrementAndGet());

        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParentClassdefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parentClassdefDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParentClassdef() throws Exception {
        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();
        parentClassdef.setId(count.incrementAndGet());

        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentClassdefMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParentClassdef() throws Exception {
        int databaseSizeBeforeUpdate = parentClassdefRepository.findAll().size();
        parentClassdef.setId(count.incrementAndGet());

        // Create the ParentClassdef
        ParentClassdefDTO parentClassdefDTO = parentClassdefMapper.toDto(parentClassdef);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParentClassdefMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parentClassdefDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParentClassdef in the database
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParentClassdef() throws Exception {
        // Initialize the database
        parentClassdefRepository.saveAndFlush(parentClassdef);

        int databaseSizeBeforeDelete = parentClassdefRepository.findAll().size();

        // Delete the parentClassdef
        restParentClassdefMockMvc
            .perform(delete(ENTITY_API_URL_ID, parentClassdef.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParentClassdef> parentClassdefList = parentClassdefRepository.findAll();
        assertThat(parentClassdefList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
