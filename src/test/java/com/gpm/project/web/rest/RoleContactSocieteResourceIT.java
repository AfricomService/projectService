package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.RoleContactSociete;
import com.gpm.project.repository.RoleContactSocieteRepository;
import com.gpm.project.service.dto.RoleContactSocieteDTO;
import com.gpm.project.service.mapper.RoleContactSocieteMapper;
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
 * Integration tests for the {@link RoleContactSocieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoleContactSocieteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/role-contact-societes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoleContactSocieteRepository roleContactSocieteRepository;

    @Autowired
    private RoleContactSocieteMapper roleContactSocieteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoleContactSocieteMockMvc;

    private RoleContactSociete roleContactSociete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleContactSociete createEntity(EntityManager em) {
        RoleContactSociete roleContactSociete = new RoleContactSociete().code(DEFAULT_CODE).label(DEFAULT_LABEL);
        return roleContactSociete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleContactSociete createUpdatedEntity(EntityManager em) {
        RoleContactSociete roleContactSociete = new RoleContactSociete().code(UPDATED_CODE).label(UPDATED_LABEL);
        return roleContactSociete;
    }

    @BeforeEach
    public void initTest() {
        roleContactSociete = createEntity(em);
    }

    @Test
    @Transactional
    void createRoleContactSociete() throws Exception {
        int databaseSizeBeforeCreate = roleContactSocieteRepository.findAll().size();
        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);
        restRoleContactSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeCreate + 1);
        RoleContactSociete testRoleContactSociete = roleContactSocieteList.get(roleContactSocieteList.size() - 1);
        assertThat(testRoleContactSociete.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRoleContactSociete.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void createRoleContactSocieteWithExistingId() throws Exception {
        // Create the RoleContactSociete with an existing ID
        roleContactSociete.setId(1L);
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        int databaseSizeBeforeCreate = roleContactSocieteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleContactSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoleContactSocietes() throws Exception {
        // Initialize the database
        roleContactSocieteRepository.saveAndFlush(roleContactSociete);

        // Get all the roleContactSocieteList
        restRoleContactSocieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleContactSociete.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }

    @Test
    @Transactional
    void getRoleContactSociete() throws Exception {
        // Initialize the database
        roleContactSocieteRepository.saveAndFlush(roleContactSociete);

        // Get the roleContactSociete
        restRoleContactSocieteMockMvc
            .perform(get(ENTITY_API_URL_ID, roleContactSociete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roleContactSociete.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    @Transactional
    void getNonExistingRoleContactSociete() throws Exception {
        // Get the roleContactSociete
        restRoleContactSocieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoleContactSociete() throws Exception {
        // Initialize the database
        roleContactSocieteRepository.saveAndFlush(roleContactSociete);

        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();

        // Update the roleContactSociete
        RoleContactSociete updatedRoleContactSociete = roleContactSocieteRepository.findById(roleContactSociete.getId()).get();
        // Disconnect from session so that the updates on updatedRoleContactSociete are not directly saved in db
        em.detach(updatedRoleContactSociete);
        updatedRoleContactSociete.code(UPDATED_CODE).label(UPDATED_LABEL);
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(updatedRoleContactSociete);

        restRoleContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleContactSocieteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isOk());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
        RoleContactSociete testRoleContactSociete = roleContactSocieteList.get(roleContactSocieteList.size() - 1);
        assertThat(testRoleContactSociete.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRoleContactSociete.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingRoleContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();
        roleContactSociete.setId(count.incrementAndGet());

        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roleContactSocieteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoleContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();
        roleContactSociete.setId(count.incrementAndGet());

        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoleContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();
        roleContactSociete.setId(count.incrementAndGet());

        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoleContactSocieteWithPatch() throws Exception {
        // Initialize the database
        roleContactSocieteRepository.saveAndFlush(roleContactSociete);

        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();

        // Update the roleContactSociete using partial update
        RoleContactSociete partialUpdatedRoleContactSociete = new RoleContactSociete();
        partialUpdatedRoleContactSociete.setId(roleContactSociete.getId());

        partialUpdatedRoleContactSociete.code(UPDATED_CODE);

        restRoleContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleContactSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleContactSociete))
            )
            .andExpect(status().isOk());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
        RoleContactSociete testRoleContactSociete = roleContactSocieteList.get(roleContactSocieteList.size() - 1);
        assertThat(testRoleContactSociete.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRoleContactSociete.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateRoleContactSocieteWithPatch() throws Exception {
        // Initialize the database
        roleContactSocieteRepository.saveAndFlush(roleContactSociete);

        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();

        // Update the roleContactSociete using partial update
        RoleContactSociete partialUpdatedRoleContactSociete = new RoleContactSociete();
        partialUpdatedRoleContactSociete.setId(roleContactSociete.getId());

        partialUpdatedRoleContactSociete.code(UPDATED_CODE).label(UPDATED_LABEL);

        restRoleContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoleContactSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoleContactSociete))
            )
            .andExpect(status().isOk());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
        RoleContactSociete testRoleContactSociete = roleContactSocieteList.get(roleContactSocieteList.size() - 1);
        assertThat(testRoleContactSociete.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRoleContactSociete.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingRoleContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();
        roleContactSociete.setId(count.incrementAndGet());

        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roleContactSocieteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoleContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();
        roleContactSociete.setId(count.incrementAndGet());

        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoleContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = roleContactSocieteRepository.findAll().size();
        roleContactSociete.setId(count.incrementAndGet());

        // Create the RoleContactSociete
        RoleContactSocieteDTO roleContactSocieteDTO = roleContactSocieteMapper.toDto(roleContactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoleContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roleContactSocieteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoleContactSociete in the database
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoleContactSociete() throws Exception {
        // Initialize the database
        roleContactSocieteRepository.saveAndFlush(roleContactSociete);

        int databaseSizeBeforeDelete = roleContactSocieteRepository.findAll().size();

        // Delete the roleContactSociete
        restRoleContactSocieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, roleContactSociete.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoleContactSociete> roleContactSocieteList = roleContactSocieteRepository.findAll();
        assertThat(roleContactSocieteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
