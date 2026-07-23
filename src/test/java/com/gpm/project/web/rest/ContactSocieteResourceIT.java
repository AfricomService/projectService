package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.ContactSociete;
import com.gpm.project.repository.ContactSocieteRepository;
import com.gpm.project.service.dto.ContactSocieteDTO;
import com.gpm.project.service.mapper.ContactSocieteMapper;
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
 * Integration tests for the {@link ContactSocieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactSocieteResourceIT {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";

    private static final Long DEFAULT_SOCIETE_ID = 1L;
    private static final Long UPDATED_SOCIETE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/contact-societes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactSocieteRepository contactSocieteRepository;

    @Autowired
    private ContactSocieteMapper contactSocieteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactSocieteMockMvc;

    private ContactSociete contactSociete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactSociete createEntity(EntityManager em) {
        ContactSociete contactSociete = new ContactSociete()
            .matricule(DEFAULT_MATRICULE)
            .nomPrenom(DEFAULT_NOM_PRENOM)
            .email(DEFAULT_EMAIL)
            .numTel(DEFAULT_NUM_TEL)
            .societeId(DEFAULT_SOCIETE_ID);
        return contactSociete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactSociete createUpdatedEntity(EntityManager em) {
        ContactSociete contactSociete = new ContactSociete()
            .matricule(UPDATED_MATRICULE)
            .nomPrenom(UPDATED_NOM_PRENOM)
            .email(UPDATED_EMAIL)
            .numTel(UPDATED_NUM_TEL)
            .societeId(UPDATED_SOCIETE_ID);
        return contactSociete;
    }

    @BeforeEach
    public void initTest() {
        contactSociete = createEntity(em);
    }

    @Test
    @Transactional
    void createContactSociete() throws Exception {
        int databaseSizeBeforeCreate = contactSocieteRepository.findAll().size();
        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);
        restContactSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeCreate + 1);
        ContactSociete testContactSociete = contactSocieteList.get(contactSocieteList.size() - 1);
        assertThat(testContactSociete.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testContactSociete.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
        assertThat(testContactSociete.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactSociete.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testContactSociete.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void createContactSocieteWithExistingId() throws Exception {
        // Create the ContactSociete with an existing ID
        contactSociete.setId(1L);
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        int databaseSizeBeforeCreate = contactSocieteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactSocieteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactSocietes() throws Exception {
        // Initialize the database
        contactSocieteRepository.saveAndFlush(contactSociete);

        // Get all the contactSocieteList
        restContactSocieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactSociete.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
            .andExpect(jsonPath("$.[*].societeId").value(hasItem(DEFAULT_SOCIETE_ID.intValue())));
    }

    @Test
    @Transactional
    void getContactSociete() throws Exception {
        // Initialize the database
        contactSocieteRepository.saveAndFlush(contactSociete);

        // Get the contactSociete
        restContactSocieteMockMvc
            .perform(get(ENTITY_API_URL_ID, contactSociete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactSociete.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
            .andExpect(jsonPath("$.societeId").value(DEFAULT_SOCIETE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingContactSociete() throws Exception {
        // Get the contactSociete
        restContactSocieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactSociete() throws Exception {
        // Initialize the database
        contactSocieteRepository.saveAndFlush(contactSociete);

        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();

        // Update the contactSociete
        ContactSociete updatedContactSociete = contactSocieteRepository.findById(contactSociete.getId()).get();
        // Disconnect from session so that the updates on updatedContactSociete are not directly saved in db
        em.detach(updatedContactSociete);
        updatedContactSociete
            .matricule(UPDATED_MATRICULE)
            .nomPrenom(UPDATED_NOM_PRENOM)
            .email(UPDATED_EMAIL)
            .numTel(UPDATED_NUM_TEL)
            .societeId(UPDATED_SOCIETE_ID);
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(updatedContactSociete);

        restContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactSocieteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
        ContactSociete testContactSociete = contactSocieteList.get(contactSocieteList.size() - 1);
        assertThat(testContactSociete.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testContactSociete.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testContactSociete.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactSociete.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testContactSociete.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
    }

    @Test
    @Transactional
    void putNonExistingContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();
        contactSociete.setId(count.incrementAndGet());

        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactSocieteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();
        contactSociete.setId(count.incrementAndGet());

        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();
        contactSociete.setId(count.incrementAndGet());

        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactSocieteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactSocieteWithPatch() throws Exception {
        // Initialize the database
        contactSocieteRepository.saveAndFlush(contactSociete);

        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();

        // Update the contactSociete using partial update
        ContactSociete partialUpdatedContactSociete = new ContactSociete();
        partialUpdatedContactSociete.setId(contactSociete.getId());

        partialUpdatedContactSociete.nomPrenom(UPDATED_NOM_PRENOM);

        restContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactSociete))
            )
            .andExpect(status().isOk());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
        ContactSociete testContactSociete = contactSocieteList.get(contactSocieteList.size() - 1);
        assertThat(testContactSociete.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testContactSociete.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testContactSociete.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContactSociete.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testContactSociete.getSocieteId()).isEqualTo(DEFAULT_SOCIETE_ID);
    }

    @Test
    @Transactional
    void fullUpdateContactSocieteWithPatch() throws Exception {
        // Initialize the database
        contactSocieteRepository.saveAndFlush(contactSociete);

        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();

        // Update the contactSociete using partial update
        ContactSociete partialUpdatedContactSociete = new ContactSociete();
        partialUpdatedContactSociete.setId(contactSociete.getId());

        partialUpdatedContactSociete
            .matricule(UPDATED_MATRICULE)
            .nomPrenom(UPDATED_NOM_PRENOM)
            .email(UPDATED_EMAIL)
            .numTel(UPDATED_NUM_TEL)
            .societeId(UPDATED_SOCIETE_ID);

        restContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactSociete.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactSociete))
            )
            .andExpect(status().isOk());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
        ContactSociete testContactSociete = contactSocieteList.get(contactSocieteList.size() - 1);
        assertThat(testContactSociete.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testContactSociete.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
        assertThat(testContactSociete.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContactSociete.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testContactSociete.getSocieteId()).isEqualTo(UPDATED_SOCIETE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();
        contactSociete.setId(count.incrementAndGet());

        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactSocieteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();
        contactSociete.setId(count.incrementAndGet());

        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactSociete() throws Exception {
        int databaseSizeBeforeUpdate = contactSocieteRepository.findAll().size();
        contactSociete.setId(count.incrementAndGet());

        // Create the ContactSociete
        ContactSocieteDTO contactSocieteDTO = contactSocieteMapper.toDto(contactSociete);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactSocieteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactSocieteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactSociete in the database
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactSociete() throws Exception {
        // Initialize the database
        contactSocieteRepository.saveAndFlush(contactSociete);

        int databaseSizeBeforeDelete = contactSocieteRepository.findAll().size();

        // Delete the contactSociete
        restContactSocieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactSociete.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactSociete> contactSocieteList = contactSocieteRepository.findAll();
        assertThat(contactSocieteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
