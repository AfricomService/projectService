package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.AclEntryProjet;
import com.gpm.project.repository.AclEntryProjetRepository;
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
 * Integration tests for the {@link AclEntryProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AclEntryProjetResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    private static final Long DEFAULT_SID_ID = 1L;
    private static final Long UPDATED_SID_ID = 2L;

    private static final String DEFAULT_SID = "AAAAAAAAAA";
    private static final String UPDATED_SID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CAN_READ = false;
    private static final Boolean UPDATED_CAN_READ = true;

    private static final Boolean DEFAULT_CAN_WRITE = false;
    private static final Boolean UPDATED_CAN_WRITE = true;

    private static final String ENTITY_API_URL = "/api/acl-entry-projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AclEntryProjetRepository aclEntryProjetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAclEntryProjetMockMvc;

    private AclEntryProjet aclEntryProjet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclEntryProjet createEntity(EntityManager em) {
        AclEntryProjet aclEntryProjet = new AclEntryProjet()
            .project_id(DEFAULT_PROJECT_ID)
            .sid_id(DEFAULT_SID_ID)
            .sid(DEFAULT_SID)
            .can_read(DEFAULT_CAN_READ)
            .can_write(DEFAULT_CAN_WRITE);
        return aclEntryProjet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AclEntryProjet createUpdatedEntity(EntityManager em) {
        AclEntryProjet aclEntryProjet = new AclEntryProjet()
            .project_id(UPDATED_PROJECT_ID)
            .sid_id(UPDATED_SID_ID)
            .sid(UPDATED_SID)
            .can_read(UPDATED_CAN_READ)
            .can_write(UPDATED_CAN_WRITE);
        return aclEntryProjet;
    }

    @BeforeEach
    public void initTest() {
        aclEntryProjet = createEntity(em);
    }

    @Test
    @Transactional
    void createAclEntryProjet() throws Exception {
        int databaseSizeBeforeCreate = aclEntryProjetRepository.findAll().size();
        // Create the AclEntryProjet
        restAclEntryProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isCreated());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeCreate + 1);
        AclEntryProjet testAclEntryProjet = aclEntryProjetList.get(aclEntryProjetList.size() - 1);
        assertThat(testAclEntryProjet.getProject_id()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testAclEntryProjet.getSid_id()).isEqualTo(DEFAULT_SID_ID);
        assertThat(testAclEntryProjet.getSid()).isEqualTo(DEFAULT_SID);
        assertThat(testAclEntryProjet.getCan_read()).isEqualTo(DEFAULT_CAN_READ);
        assertThat(testAclEntryProjet.getCan_write()).isEqualTo(DEFAULT_CAN_WRITE);
    }

    @Test
    @Transactional
    void createAclEntryProjetWithExistingId() throws Exception {
        // Create the AclEntryProjet with an existing ID
        aclEntryProjet.setId(1L);

        int databaseSizeBeforeCreate = aclEntryProjetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAclEntryProjetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAclEntryProjets() throws Exception {
        // Initialize the database
        aclEntryProjetRepository.saveAndFlush(aclEntryProjet);

        // Get all the aclEntryProjetList
        restAclEntryProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aclEntryProjet.getId().intValue())))
            .andExpect(jsonPath("$.[*].project_id").value(hasItem(DEFAULT_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sid_id").value(hasItem(DEFAULT_SID_ID.intValue())))
            .andExpect(jsonPath("$.[*].sid").value(hasItem(DEFAULT_SID)))
            .andExpect(jsonPath("$.[*].can_read").value(hasItem(DEFAULT_CAN_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].can_write").value(hasItem(DEFAULT_CAN_WRITE.booleanValue())));
    }

    @Test
    @Transactional
    void getAclEntryProjet() throws Exception {
        // Initialize the database
        aclEntryProjetRepository.saveAndFlush(aclEntryProjet);

        // Get the aclEntryProjet
        restAclEntryProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, aclEntryProjet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aclEntryProjet.getId().intValue()))
            .andExpect(jsonPath("$.project_id").value(DEFAULT_PROJECT_ID.intValue()))
            .andExpect(jsonPath("$.sid_id").value(DEFAULT_SID_ID.intValue()))
            .andExpect(jsonPath("$.sid").value(DEFAULT_SID))
            .andExpect(jsonPath("$.can_read").value(DEFAULT_CAN_READ.booleanValue()))
            .andExpect(jsonPath("$.can_write").value(DEFAULT_CAN_WRITE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAclEntryProjet() throws Exception {
        // Get the aclEntryProjet
        restAclEntryProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAclEntryProjet() throws Exception {
        // Initialize the database
        aclEntryProjetRepository.saveAndFlush(aclEntryProjet);

        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();

        // Update the aclEntryProjet
        AclEntryProjet updatedAclEntryProjet = aclEntryProjetRepository.findById(aclEntryProjet.getId()).get();
        // Disconnect from session so that the updates on updatedAclEntryProjet are not directly saved in db
        em.detach(updatedAclEntryProjet);
        updatedAclEntryProjet
            .project_id(UPDATED_PROJECT_ID)
            .sid_id(UPDATED_SID_ID)
            .sid(UPDATED_SID)
            .can_read(UPDATED_CAN_READ)
            .can_write(UPDATED_CAN_WRITE);

        restAclEntryProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAclEntryProjet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAclEntryProjet))
            )
            .andExpect(status().isOk());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
        AclEntryProjet testAclEntryProjet = aclEntryProjetList.get(aclEntryProjetList.size() - 1);
        assertThat(testAclEntryProjet.getProject_id()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testAclEntryProjet.getSid_id()).isEqualTo(UPDATED_SID_ID);
        assertThat(testAclEntryProjet.getSid()).isEqualTo(UPDATED_SID);
        assertThat(testAclEntryProjet.getCan_read()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAclEntryProjet.getCan_write()).isEqualTo(UPDATED_CAN_WRITE);
    }

    @Test
    @Transactional
    void putNonExistingAclEntryProjet() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();
        aclEntryProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclEntryProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aclEntryProjet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAclEntryProjet() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();
        aclEntryProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAclEntryProjet() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();
        aclEntryProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryProjetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAclEntryProjetWithPatch() throws Exception {
        // Initialize the database
        aclEntryProjetRepository.saveAndFlush(aclEntryProjet);

        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();

        // Update the aclEntryProjet using partial update
        AclEntryProjet partialUpdatedAclEntryProjet = new AclEntryProjet();
        partialUpdatedAclEntryProjet.setId(aclEntryProjet.getId());

        partialUpdatedAclEntryProjet.sid_id(UPDATED_SID_ID).sid(UPDATED_SID);

        restAclEntryProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclEntryProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclEntryProjet))
            )
            .andExpect(status().isOk());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
        AclEntryProjet testAclEntryProjet = aclEntryProjetList.get(aclEntryProjetList.size() - 1);
        assertThat(testAclEntryProjet.getProject_id()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testAclEntryProjet.getSid_id()).isEqualTo(UPDATED_SID_ID);
        assertThat(testAclEntryProjet.getSid()).isEqualTo(UPDATED_SID);
        assertThat(testAclEntryProjet.getCan_read()).isEqualTo(DEFAULT_CAN_READ);
        assertThat(testAclEntryProjet.getCan_write()).isEqualTo(DEFAULT_CAN_WRITE);
    }

    @Test
    @Transactional
    void fullUpdateAclEntryProjetWithPatch() throws Exception {
        // Initialize the database
        aclEntryProjetRepository.saveAndFlush(aclEntryProjet);

        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();

        // Update the aclEntryProjet using partial update
        AclEntryProjet partialUpdatedAclEntryProjet = new AclEntryProjet();
        partialUpdatedAclEntryProjet.setId(aclEntryProjet.getId());

        partialUpdatedAclEntryProjet
            .project_id(UPDATED_PROJECT_ID)
            .sid_id(UPDATED_SID_ID)
            .sid(UPDATED_SID)
            .can_read(UPDATED_CAN_READ)
            .can_write(UPDATED_CAN_WRITE);

        restAclEntryProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAclEntryProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAclEntryProjet))
            )
            .andExpect(status().isOk());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
        AclEntryProjet testAclEntryProjet = aclEntryProjetList.get(aclEntryProjetList.size() - 1);
        assertThat(testAclEntryProjet.getProject_id()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testAclEntryProjet.getSid_id()).isEqualTo(UPDATED_SID_ID);
        assertThat(testAclEntryProjet.getSid()).isEqualTo(UPDATED_SID);
        assertThat(testAclEntryProjet.getCan_read()).isEqualTo(UPDATED_CAN_READ);
        assertThat(testAclEntryProjet.getCan_write()).isEqualTo(UPDATED_CAN_WRITE);
    }

    @Test
    @Transactional
    void patchNonExistingAclEntryProjet() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();
        aclEntryProjet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAclEntryProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aclEntryProjet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAclEntryProjet() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();
        aclEntryProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAclEntryProjet() throws Exception {
        int databaseSizeBeforeUpdate = aclEntryProjetRepository.findAll().size();
        aclEntryProjet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAclEntryProjetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aclEntryProjet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AclEntryProjet in the database
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAclEntryProjet() throws Exception {
        // Initialize the database
        aclEntryProjetRepository.saveAndFlush(aclEntryProjet);

        int databaseSizeBeforeDelete = aclEntryProjetRepository.findAll().size();

        // Delete the aclEntryProjet
        restAclEntryProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, aclEntryProjet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AclEntryProjet> aclEntryProjetList = aclEntryProjetRepository.findAll();
        assertThat(aclEntryProjetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
