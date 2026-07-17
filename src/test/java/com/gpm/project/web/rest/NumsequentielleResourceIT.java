package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Numsequentielle;
import com.gpm.project.repository.NumsequentielleRepository;
import com.gpm.project.service.dto.NumsequentielleDTO;
import com.gpm.project.service.mapper.NumsequentielleMapper;
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
 * Integration tests for the {@link NumsequentielleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NumsequentielleResourceIT {

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final String DEFAULT_NEXT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_NUM_SEQ = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NUM_SEQ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/numsequentielles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NumsequentielleRepository numsequentielleRepository;

    @Autowired
    private NumsequentielleMapper numsequentielleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNumsequentielleMockMvc;

    private Numsequentielle numsequentielle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Numsequentielle createEntity(EntityManager em) {
        Numsequentielle numsequentielle = new Numsequentielle()
            .prefix(DEFAULT_PREFIX)
            .nextNumber(DEFAULT_NEXT_NUMBER)
            .format(DEFAULT_FORMAT)
            .codeNumSeq(DEFAULT_CODE_NUM_SEQ);
        return numsequentielle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Numsequentielle createUpdatedEntity(EntityManager em) {
        Numsequentielle numsequentielle = new Numsequentielle()
            .prefix(UPDATED_PREFIX)
            .nextNumber(UPDATED_NEXT_NUMBER)
            .format(UPDATED_FORMAT)
            .codeNumSeq(UPDATED_CODE_NUM_SEQ);
        return numsequentielle;
    }

    @BeforeEach
    public void initTest() {
        numsequentielle = createEntity(em);
    }

    @Test
    @Transactional
    void createNumsequentielle() throws Exception {
        int databaseSizeBeforeCreate = numsequentielleRepository.findAll().size();
        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);
        restNumsequentielleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeCreate + 1);
        Numsequentielle testNumsequentielle = numsequentielleList.get(numsequentielleList.size() - 1);
        assertThat(testNumsequentielle.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testNumsequentielle.getNextNumber()).isEqualTo(DEFAULT_NEXT_NUMBER);
        assertThat(testNumsequentielle.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testNumsequentielle.getCodeNumSeq()).isEqualTo(DEFAULT_CODE_NUM_SEQ);
    }

    @Test
    @Transactional
    void createNumsequentielleWithExistingId() throws Exception {
        // Create the Numsequentielle with an existing ID
        numsequentielle.setId(1L);
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        int databaseSizeBeforeCreate = numsequentielleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNumsequentielleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNextNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = numsequentielleRepository.findAll().size();
        // set the field null
        numsequentielle.setNextNumber(null);

        // Create the Numsequentielle, which fails.
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        restNumsequentielleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isBadRequest());

        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNumsequentielles() throws Exception {
        // Initialize the database
        numsequentielleRepository.saveAndFlush(numsequentielle);

        // Get all the numsequentielleList
        restNumsequentielleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(numsequentielle.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].nextNumber").value(hasItem(DEFAULT_NEXT_NUMBER)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT)))
            .andExpect(jsonPath("$.[*].codeNumSeq").value(hasItem(DEFAULT_CODE_NUM_SEQ)));
    }

    @Test
    @Transactional
    void getNumsequentielle() throws Exception {
        // Initialize the database
        numsequentielleRepository.saveAndFlush(numsequentielle);

        // Get the numsequentielle
        restNumsequentielleMockMvc
            .perform(get(ENTITY_API_URL_ID, numsequentielle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(numsequentielle.getId().intValue()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX))
            .andExpect(jsonPath("$.nextNumber").value(DEFAULT_NEXT_NUMBER))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT))
            .andExpect(jsonPath("$.codeNumSeq").value(DEFAULT_CODE_NUM_SEQ));
    }

    @Test
    @Transactional
    void getNonExistingNumsequentielle() throws Exception {
        // Get the numsequentielle
        restNumsequentielleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNumsequentielle() throws Exception {
        // Initialize the database
        numsequentielleRepository.saveAndFlush(numsequentielle);

        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();

        // Update the numsequentielle
        Numsequentielle updatedNumsequentielle = numsequentielleRepository.findById(numsequentielle.getId()).get();
        // Disconnect from session so that the updates on updatedNumsequentielle are not directly saved in db
        em.detach(updatedNumsequentielle);
        updatedNumsequentielle
            .prefix(UPDATED_PREFIX)
            .nextNumber(UPDATED_NEXT_NUMBER)
            .format(UPDATED_FORMAT)
            .codeNumSeq(UPDATED_CODE_NUM_SEQ);
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(updatedNumsequentielle);

        restNumsequentielleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, numsequentielleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
        Numsequentielle testNumsequentielle = numsequentielleList.get(numsequentielleList.size() - 1);
        assertThat(testNumsequentielle.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testNumsequentielle.getNextNumber()).isEqualTo(UPDATED_NEXT_NUMBER);
        assertThat(testNumsequentielle.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testNumsequentielle.getCodeNumSeq()).isEqualTo(UPDATED_CODE_NUM_SEQ);
    }

    @Test
    @Transactional
    void putNonExistingNumsequentielle() throws Exception {
        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();
        numsequentielle.setId(count.incrementAndGet());

        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNumsequentielleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, numsequentielleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNumsequentielle() throws Exception {
        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();
        numsequentielle.setId(count.incrementAndGet());

        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumsequentielleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNumsequentielle() throws Exception {
        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();
        numsequentielle.setId(count.incrementAndGet());

        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumsequentielleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNumsequentielleWithPatch() throws Exception {
        // Initialize the database
        numsequentielleRepository.saveAndFlush(numsequentielle);

        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();

        // Update the numsequentielle using partial update
        Numsequentielle partialUpdatedNumsequentielle = new Numsequentielle();
        partialUpdatedNumsequentielle.setId(numsequentielle.getId());

        partialUpdatedNumsequentielle.format(UPDATED_FORMAT);

        restNumsequentielleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNumsequentielle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNumsequentielle))
            )
            .andExpect(status().isOk());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
        Numsequentielle testNumsequentielle = numsequentielleList.get(numsequentielleList.size() - 1);
        assertThat(testNumsequentielle.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testNumsequentielle.getNextNumber()).isEqualTo(DEFAULT_NEXT_NUMBER);
        assertThat(testNumsequentielle.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testNumsequentielle.getCodeNumSeq()).isEqualTo(DEFAULT_CODE_NUM_SEQ);
    }

    @Test
    @Transactional
    void fullUpdateNumsequentielleWithPatch() throws Exception {
        // Initialize the database
        numsequentielleRepository.saveAndFlush(numsequentielle);

        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();

        // Update the numsequentielle using partial update
        Numsequentielle partialUpdatedNumsequentielle = new Numsequentielle();
        partialUpdatedNumsequentielle.setId(numsequentielle.getId());

        partialUpdatedNumsequentielle
            .prefix(UPDATED_PREFIX)
            .nextNumber(UPDATED_NEXT_NUMBER)
            .format(UPDATED_FORMAT)
            .codeNumSeq(UPDATED_CODE_NUM_SEQ);

        restNumsequentielleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNumsequentielle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNumsequentielle))
            )
            .andExpect(status().isOk());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
        Numsequentielle testNumsequentielle = numsequentielleList.get(numsequentielleList.size() - 1);
        assertThat(testNumsequentielle.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testNumsequentielle.getNextNumber()).isEqualTo(UPDATED_NEXT_NUMBER);
        assertThat(testNumsequentielle.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testNumsequentielle.getCodeNumSeq()).isEqualTo(UPDATED_CODE_NUM_SEQ);
    }

    @Test
    @Transactional
    void patchNonExistingNumsequentielle() throws Exception {
        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();
        numsequentielle.setId(count.incrementAndGet());

        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNumsequentielleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, numsequentielleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNumsequentielle() throws Exception {
        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();
        numsequentielle.setId(count.incrementAndGet());

        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumsequentielleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNumsequentielle() throws Exception {
        int databaseSizeBeforeUpdate = numsequentielleRepository.findAll().size();
        numsequentielle.setId(count.incrementAndGet());

        // Create the Numsequentielle
        NumsequentielleDTO numsequentielleDTO = numsequentielleMapper.toDto(numsequentielle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumsequentielleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(numsequentielleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Numsequentielle in the database
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNumsequentielle() throws Exception {
        // Initialize the database
        numsequentielleRepository.saveAndFlush(numsequentielle);

        int databaseSizeBeforeDelete = numsequentielleRepository.findAll().size();

        // Delete the numsequentielle
        restNumsequentielleMockMvc
            .perform(delete(ENTITY_API_URL_ID, numsequentielle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Numsequentielle> numsequentielleList = numsequentielleRepository.findAll();
        assertThat(numsequentielleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
