package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Ville;
import com.gpm.project.domain.Zone;
import com.gpm.project.repository.ZoneRepository;
import com.gpm.project.service.ZoneService;
import com.gpm.project.service.dto.ZoneDTO;
import com.gpm.project.service.mapper.ZoneMapper;
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
 * Integration tests for the {@link ZoneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ZoneResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ZoneRepository zoneRepository;

    @Mock
    private ZoneRepository zoneRepositoryMock;

    @Autowired
    private ZoneMapper zoneMapper;

    @Mock
    private ZoneService zoneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restZoneMockMvc;

    private Zone zone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createEntity(EntityManager em) {
        Zone zone = new Zone().nom(DEFAULT_NOM);
        // Add required entity
        Ville ville;
        if (TestUtil.findAll(em, Ville.class).isEmpty()) {
            ville = VilleResourceIT.createEntity(em);
            em.persist(ville);
            em.flush();
        } else {
            ville = TestUtil.findAll(em, Ville.class).get(0);
        }
        zone.setVille(ville);
        return zone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Zone createUpdatedEntity(EntityManager em) {
        Zone zone = new Zone().nom(UPDATED_NOM);
        // Add required entity
        Ville ville;
        if (TestUtil.findAll(em, Ville.class).isEmpty()) {
            ville = VilleResourceIT.createUpdatedEntity(em);
            em.persist(ville);
            em.flush();
        } else {
            ville = TestUtil.findAll(em, Ville.class).get(0);
        }
        zone.setVille(ville);
        return zone;
    }

    @BeforeEach
    public void initTest() {
        zone = createEntity(em);
    }

    @Test
    @Transactional
    void createZone() throws Exception {
        int databaseSizeBeforeCreate = zoneRepository.findAll().size();
        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);
        restZoneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate + 1);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createZoneWithExistingId() throws Exception {
        // Create the Zone with an existing ID
        zone.setId(1L);
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        int databaseSizeBeforeCreate = zoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restZoneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = zoneRepository.findAll().size();
        // set the field null
        zone.setNom(null);

        // Create the Zone, which fails.
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        restZoneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllZones() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get all the zoneList
        restZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(zone.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZonesWithEagerRelationshipsIsEnabled() throws Exception {
        when(zoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(zoneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllZonesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(zoneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(zoneRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        // Get the zone
        restZoneMockMvc
            .perform(get(ENTITY_API_URL_ID, zone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(zone.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingZone() throws Exception {
        // Get the zone
        restZoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone
        Zone updatedZone = zoneRepository.findById(zone.getId()).get();
        // Disconnect from session so that the updates on updatedZone are not directly saved in db
        em.detach(updatedZone);
        updatedZone.nom(UPDATED_NOM);
        ZoneDTO zoneDTO = zoneMapper.toDto(updatedZone);

        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zoneDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, zoneDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateZoneWithPatch() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone using partial update
        Zone partialUpdatedZone = new Zone();
        partialUpdatedZone.setId(zone.getId());

        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZone.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateZoneWithPatch() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();

        // Update the zone using partial update
        Zone partialUpdatedZone = new Zone();
        partialUpdatedZone.setId(zone.getId());

        partialUpdatedZone.nom(UPDATED_NOM);

        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedZone.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedZone))
            )
            .andExpect(status().isOk());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
        Zone testZone = zoneList.get(zoneList.size() - 1);
        assertThat(testZone.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, zoneDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamZone() throws Exception {
        int databaseSizeBeforeUpdate = zoneRepository.findAll().size();
        zone.setId(count.incrementAndGet());

        // Create the Zone
        ZoneDTO zoneDTO = zoneMapper.toDto(zone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restZoneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(zoneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Zone in the database
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteZone() throws Exception {
        // Initialize the database
        zoneRepository.saveAndFlush(zone);

        int databaseSizeBeforeDelete = zoneRepository.findAll().size();

        // Delete the zone
        restZoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, zone.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Zone> zoneList = zoneRepository.findAll();
        assertThat(zoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
