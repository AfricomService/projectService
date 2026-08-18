package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.DetailRessource;
import com.gpm.project.repository.DetailRessourceRepository;
import com.gpm.project.service.dto.DetailRessourceDTO;
import com.gpm.project.service.mapper.DetailRessourceMapper;
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
 * Integration tests for the {@link DetailRessourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailRessourceResourceIT {

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUIRED = false;
    private static final Boolean UPDATED_REQUIRED = true;

    private static final String DEFAULT_INPUT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MULTIPLE_CHOICE_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_MULTIPLE_CHOICE_OPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/detail-ressources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailRessourceRepository detailRessourceRepository;

    @Autowired
    private DetailRessourceMapper detailRessourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailRessourceMockMvc;

    private DetailRessource detailRessource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailRessource createEntity(EntityManager em) {
        DetailRessource detailRessource = new DetailRessource()
            .status(DEFAULT_STATUS)
            .label(DEFAULT_LABEL)
            .code(DEFAULT_CODE)
            .required(DEFAULT_REQUIRED)
            .inputType(DEFAULT_INPUT_TYPE)
            .multipleChoiceOption(DEFAULT_MULTIPLE_CHOICE_OPTION);
        return detailRessource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailRessource createUpdatedEntity(EntityManager em) {
        DetailRessource detailRessource = new DetailRessource()
            .status(UPDATED_STATUS)
            .label(UPDATED_LABEL)
            .code(UPDATED_CODE)
            .required(UPDATED_REQUIRED)
            .inputType(UPDATED_INPUT_TYPE)
            .multipleChoiceOption(UPDATED_MULTIPLE_CHOICE_OPTION);
        return detailRessource;
    }

    @BeforeEach
    public void initTest() {
        detailRessource = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailRessource() throws Exception {
        int databaseSizeBeforeCreate = detailRessourceRepository.findAll().size();
        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);
        restDetailRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeCreate + 1);
        DetailRessource testDetailRessource = detailRessourceList.get(detailRessourceList.size() - 1);
        assertThat(testDetailRessource.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDetailRessource.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testDetailRessource.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDetailRessource.getRequired()).isEqualTo(DEFAULT_REQUIRED);
        assertThat(testDetailRessource.getInputType()).isEqualTo(DEFAULT_INPUT_TYPE);
        assertThat(testDetailRessource.getMultipleChoiceOption()).isEqualTo(DEFAULT_MULTIPLE_CHOICE_OPTION);
    }

    @Test
    @Transactional
    void createDetailRessourceWithExistingId() throws Exception {
        // Create the DetailRessource with an existing ID
        detailRessource.setId(1L);
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        int databaseSizeBeforeCreate = detailRessourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailRessourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetailRessources() throws Exception {
        // Initialize the database
        detailRessourceRepository.saveAndFlush(detailRessource);

        // Get all the detailRessourceList
        restDetailRessourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailRessource.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].inputType").value(hasItem(DEFAULT_INPUT_TYPE)))
            .andExpect(jsonPath("$.[*].multipleChoiceOption").value(hasItem(DEFAULT_MULTIPLE_CHOICE_OPTION)));
    }

    @Test
    @Transactional
    void getDetailRessource() throws Exception {
        // Initialize the database
        detailRessourceRepository.saveAndFlush(detailRessource);

        // Get the detailRessource
        restDetailRessourceMockMvc
            .perform(get(ENTITY_API_URL_ID, detailRessource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailRessource.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.required").value(DEFAULT_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.inputType").value(DEFAULT_INPUT_TYPE))
            .andExpect(jsonPath("$.multipleChoiceOption").value(DEFAULT_MULTIPLE_CHOICE_OPTION));
    }

    @Test
    @Transactional
    void getNonExistingDetailRessource() throws Exception {
        // Get the detailRessource
        restDetailRessourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailRessource() throws Exception {
        // Initialize the database
        detailRessourceRepository.saveAndFlush(detailRessource);

        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();

        // Update the detailRessource
        DetailRessource updatedDetailRessource = detailRessourceRepository.findById(detailRessource.getId()).get();
        // Disconnect from session so that the updates on updatedDetailRessource are not directly saved in db
        em.detach(updatedDetailRessource);
        updatedDetailRessource
            .status(UPDATED_STATUS)
            .label(UPDATED_LABEL)
            .code(UPDATED_CODE)
            .required(UPDATED_REQUIRED)
            .inputType(UPDATED_INPUT_TYPE)
            .multipleChoiceOption(UPDATED_MULTIPLE_CHOICE_OPTION);
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(updatedDetailRessource);

        restDetailRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailRessourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
        DetailRessource testDetailRessource = detailRessourceList.get(detailRessourceList.size() - 1);
        assertThat(testDetailRessource.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDetailRessource.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testDetailRessource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDetailRessource.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testDetailRessource.getInputType()).isEqualTo(UPDATED_INPUT_TYPE);
        assertThat(testDetailRessource.getMultipleChoiceOption()).isEqualTo(UPDATED_MULTIPLE_CHOICE_OPTION);
    }

    @Test
    @Transactional
    void putNonExistingDetailRessource() throws Exception {
        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();
        detailRessource.setId(count.incrementAndGet());

        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailRessourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailRessource() throws Exception {
        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();
        detailRessource.setId(count.incrementAndGet());

        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailRessourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailRessource() throws Exception {
        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();
        detailRessource.setId(count.incrementAndGet());

        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailRessourceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailRessourceWithPatch() throws Exception {
        // Initialize the database
        detailRessourceRepository.saveAndFlush(detailRessource);

        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();

        // Update the detailRessource using partial update
        DetailRessource partialUpdatedDetailRessource = new DetailRessource();
        partialUpdatedDetailRessource.setId(detailRessource.getId());

        partialUpdatedDetailRessource.status(UPDATED_STATUS).label(UPDATED_LABEL).code(UPDATED_CODE);

        restDetailRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailRessource))
            )
            .andExpect(status().isOk());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
        DetailRessource testDetailRessource = detailRessourceList.get(detailRessourceList.size() - 1);
        assertThat(testDetailRessource.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDetailRessource.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testDetailRessource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDetailRessource.getRequired()).isEqualTo(DEFAULT_REQUIRED);
        assertThat(testDetailRessource.getInputType()).isEqualTo(DEFAULT_INPUT_TYPE);
        assertThat(testDetailRessource.getMultipleChoiceOption()).isEqualTo(DEFAULT_MULTIPLE_CHOICE_OPTION);
    }

    @Test
    @Transactional
    void fullUpdateDetailRessourceWithPatch() throws Exception {
        // Initialize the database
        detailRessourceRepository.saveAndFlush(detailRessource);

        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();

        // Update the detailRessource using partial update
        DetailRessource partialUpdatedDetailRessource = new DetailRessource();
        partialUpdatedDetailRessource.setId(detailRessource.getId());

        partialUpdatedDetailRessource
            .status(UPDATED_STATUS)
            .label(UPDATED_LABEL)
            .code(UPDATED_CODE)
            .required(UPDATED_REQUIRED)
            .inputType(UPDATED_INPUT_TYPE)
            .multipleChoiceOption(UPDATED_MULTIPLE_CHOICE_OPTION);

        restDetailRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailRessource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailRessource))
            )
            .andExpect(status().isOk());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
        DetailRessource testDetailRessource = detailRessourceList.get(detailRessourceList.size() - 1);
        assertThat(testDetailRessource.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDetailRessource.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testDetailRessource.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDetailRessource.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testDetailRessource.getInputType()).isEqualTo(UPDATED_INPUT_TYPE);
        assertThat(testDetailRessource.getMultipleChoiceOption()).isEqualTo(UPDATED_MULTIPLE_CHOICE_OPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDetailRessource() throws Exception {
        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();
        detailRessource.setId(count.incrementAndGet());

        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailRessourceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailRessource() throws Exception {
        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();
        detailRessource.setId(count.incrementAndGet());

        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailRessource() throws Exception {
        int databaseSizeBeforeUpdate = detailRessourceRepository.findAll().size();
        detailRessource.setId(count.incrementAndGet());

        // Create the DetailRessource
        DetailRessourceDTO detailRessourceDTO = detailRessourceMapper.toDto(detailRessource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailRessourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailRessourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailRessource in the database
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailRessource() throws Exception {
        // Initialize the database
        detailRessourceRepository.saveAndFlush(detailRessource);

        int databaseSizeBeforeDelete = detailRessourceRepository.findAll().size();

        // Delete the detailRessource
        restDetailRessourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailRessource.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailRessource> detailRessourceList = detailRessourceRepository.findAll();
        assertThat(detailRessourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
