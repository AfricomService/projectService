package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.UserAuthSociete;
import com.gpm.project.repository.UserAuthSocieteRepository;
import com.gpm.project.service.dto.UserAuthSocieteDTO;
import com.gpm.project.service.mapper.UserAuthSocieteMapper;
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
 * Integration tests for the {@link UserAuthSocieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserAuthSocieteResourceIT {

    private static final Long DEFAULT_SOCIETE_ID = 1L;
    private static final Long UPDATED_SOCIETE_ID = 2L;

    private static final Long DEFAULT_ROLE_CONTACT_SOCIETE_ID = 1L;
    private static final Long UPDATED_ROLE_CONTACT_SOCIETE_ID = 2L;

    private static final Long DEFAULT_CONTACT_SOCIETE_ID = 1L;
    private static final Long UPDATED_CONTACT_SOCIETE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-auth-societes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAuthSocieteRepository userAuthSocieteRepository;

    @Autowired
    private UserAuthSocieteMapper userAuthSocieteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAuthSocieteMockMvc;

    private UserAuthSociete userAuthSociete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAuthSociete createEntity(EntityManager em) {
        UserAuthSociete userAuthSociete = new UserAuthSociete()
            .societeId(DEFAULT_SOCIETE_ID)
            .roleContactSocieteId(DEFAULT_ROLE_CONTACT_SOCIETE_ID)
            .contactSocieteId(DEFAULT_CONTACT_SOCIETE_ID);
        return userAuthSociete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAuthSociete createUpdatedEntity(EntityManager em) {
        UserAuthSociete userAuthSociete = new UserAuthSociete()
            .societeId(UPDATED_SOCIETE_ID)
            .roleContactSocieteId(UPDATED_ROLE_CONTACT_SOCIETE_ID)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID);
        return userAuthSociete;
    }

    @BeforeEach
    public void initTest() {
        userAuthSociete = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAuthSociete() throws Exception {
        int databaseSizeBeforeCreate = userAuthSocieteRepository.findAll().size();
        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);
        restUserAuthSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeCreate + 1);
        UserAuthSociete testUserAuthSociete = userAuthSocieteList.get(userAuthSocieteList.size() - 1);
        assertThat(testUserAuthSociete.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
        assertThat(testUserAuthSociete.getRoleContactSocieteId()).isEqualTo(DEFAULT_ROLE_CONTACT_SOCIETE_ID);
        assertThat(testUserAuthSociete.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void createUserAuthSocieteWithExistingId() throws Exception {
        // Create the UserAuthSociete with an existing ID
        userAuthSociete.setId(1L);
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        int databaseSizeBeforeCreate = userAuthSocieteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAuthSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserAuthSocietes() throws Exception {
        // Initialize the database
        userAuthSocieteRepository.saveAndFlush(userAuthSociete);

        // Get all the userAuthSocieteList
        restUserAuthSocieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAuthSociete.getId().intValue())))
            .andExpect(jsonPath("$.[*].societeId").value(hasItem(DEFAULT_SOCIETE_ID.intValue())))
            .andExpect(jsonPath("$.[*].roleContactSocieteId").value(hasItem(DEFAULT_ROLE_CONTACT_SOCIETE_ID.intValue())))
            .andExpect(jsonPath("$.[*].contactSocieteId").value(hasItem(DEFAULT_CONTACT_SOCIETE_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserAuthSociete() throws Exception {
        // Initialize the database
        userAuthSocieteRepository.saveAndFlush(userAuthSociete);

        // Get the userAuthSociete
        restUserAuthSocieteMockMvc
            .perform(get(ENTITY_API_URL_ID, userAuthSociete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAuthSociete.getId().intValue()))
            .andExpect(jsonPath("$.societeId").value(DEFAULT_SOCIETE_ID.intValue()))
            .andExpect(jsonPath("$.roleContactSocieteId").value(DEFAULT_ROLE_CONTACT_SOCIETE_ID.intValue()))
            .andExpect(jsonPath("$.contactSocieteId").value(DEFAULT_CONTACT_SOCIETE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserAuthSociete() throws Exception {
        // Get the userAuthSociete
        restUserAuthSocieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAuthSociete() throws Exception {
        // Initialize the database
        userAuthSocieteRepository.saveAndFlush(userAuthSociete);

        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();

        // Update the userAuthSociete
        UserAuthSociete updatedUserAuthSociete = userAuthSocieteRepository.findById(userAuthSociete.getId()).get();
        // Disconnect from session so that the updates on updatedUserAuthSociete are not directly saved in db
        em.detach(updatedUserAuthSociete);
        updatedUserAuthSociete
            .societeId(UPDATED_SOCIETE_ID)
            .roleContactSocieteId(UPDATED_ROLE_CONTACT_SOCIETE_ID)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID);
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(updatedUserAuthSociete);

        restUserAuthSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAuthSocieteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
        UserAuthSociete testUserAuthSociete = userAuthSocieteList.get(userAuthSocieteList.size() - 1);
        assertThat(testUserAuthSociete.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
        assertThat(testUserAuthSociete.getRoleContactSocieteId()).isEqualTo(UPDATED_ROLE_CONTACT_SOCIETE_ID);
        assertThat(testUserAuthSociete.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserAuthSociete() throws Exception {
        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();
        userAuthSociete.setId(count.incrementAndGet());

        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAuthSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAuthSocieteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAuthSociete() throws Exception {
        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();
        userAuthSociete.setId(count.incrementAndGet());

        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAuthSociete() throws Exception {
        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();
        userAuthSociete.setId(count.incrementAndGet());

        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthSocieteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAuthSocieteWithPatch() throws Exception {
        // Initialize the database
        userAuthSocieteRepository.saveAndFlush(userAuthSociete);

        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();

        // Update the userAuthSociete using partial update
        UserAuthSociete partialUpdatedUserAuthSociete = new UserAuthSociete();
        partialUpdatedUserAuthSociete.setId(userAuthSociete.getId());

        partialUpdatedUserAuthSociete.roleContactSocieteId(UPDATED_ROLE_CONTACT_SOCIETE_ID);

        restUserAuthSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAuthSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuthSociete))
            )
            .andExpect(status().isOk());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
        UserAuthSociete testUserAuthSociete = userAuthSocieteList.get(userAuthSocieteList.size() - 1);
        assertThat(testUserAuthSociete.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
        assertThat(testUserAuthSociete.getRoleContactSocieteId()).isEqualTo(UPDATED_ROLE_CONTACT_SOCIETE_ID);
        assertThat(testUserAuthSociete.getContactSocieteId()).isEqualTo(DEFAULT_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserAuthSocieteWithPatch() throws Exception {
        // Initialize the database
        userAuthSocieteRepository.saveAndFlush(userAuthSociete);

        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();

        // Update the userAuthSociete using partial update
        UserAuthSociete partialUpdatedUserAuthSociete = new UserAuthSociete();
        partialUpdatedUserAuthSociete.setId(userAuthSociete.getId());

        partialUpdatedUserAuthSociete
            .societeId(UPDATED_SOCIETE_ID)
            .roleContactSocieteId(UPDATED_ROLE_CONTACT_SOCIETE_ID)
            .contactSocieteId(UPDATED_CONTACT_SOCIETE_ID);

        restUserAuthSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAuthSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAuthSociete))
            )
            .andExpect(status().isOk());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
        UserAuthSociete testUserAuthSociete = userAuthSocieteList.get(userAuthSocieteList.size() - 1);
        assertThat(testUserAuthSociete.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
        assertThat(testUserAuthSociete.getRoleContactSocieteId()).isEqualTo(UPDATED_ROLE_CONTACT_SOCIETE_ID);
        assertThat(testUserAuthSociete.getContactSocieteId()).isEqualTo(UPDATED_CONTACT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserAuthSociete() throws Exception {
        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();
        userAuthSociete.setId(count.incrementAndGet());

        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAuthSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAuthSocieteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAuthSociete() throws Exception {
        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();
        userAuthSociete.setId(count.incrementAndGet());

        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAuthSociete() throws Exception {
        int databaseSizeBeforeUpdate = userAuthSocieteRepository.findAll().size();
        userAuthSociete.setId(count.incrementAndGet());

        // Create the UserAuthSociete
        UserAuthSocieteDTO userAuthSocieteDTO = userAuthSocieteMapper.toDto(userAuthSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAuthSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userAuthSocieteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAuthSociete in the database
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAuthSociete() throws Exception {
        // Initialize the database
        userAuthSocieteRepository.saveAndFlush(userAuthSociete);

        int databaseSizeBeforeDelete = userAuthSocieteRepository.findAll().size();

        // Delete the userAuthSociete
        restUserAuthSocieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAuthSociete.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserAuthSociete> userAuthSocieteList = userAuthSocieteRepository.findAll();
        assertThat(userAuthSocieteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
