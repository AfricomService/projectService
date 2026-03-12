package com.gpm.project.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gpm.project.IntegrationTest;
import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.AffaireArticle;
import com.gpm.project.domain.Article;
import com.gpm.project.repository.AffaireArticleRepository;
import com.gpm.project.service.AffaireArticleService;
import com.gpm.project.service.dto.AffaireArticleDTO;
import com.gpm.project.service.mapper.AffaireArticleMapper;
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
 * Integration tests for the {@link AffaireArticleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AffaireArticleResourceIT {

    private static final Float DEFAULT_QUANTITE_CONTRACTUELLE = 1F;
    private static final Float UPDATED_QUANTITE_CONTRACTUELLE = 2F;

    private static final Float DEFAULT_QUANTITE_REALISEE = 1F;
    private static final Float UPDATED_QUANTITE_REALISEE = 2F;

    private static final String ENTITY_API_URL = "/api/affaire-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffaireArticleRepository affaireArticleRepository;

    @Mock
    private AffaireArticleRepository affaireArticleRepositoryMock;

    @Autowired
    private AffaireArticleMapper affaireArticleMapper;

    @Mock
    private AffaireArticleService affaireArticleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffaireArticleMockMvc;

    private AffaireArticle affaireArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffaireArticle createEntity(EntityManager em) {
        AffaireArticle affaireArticle = new AffaireArticle()
            .quantiteContractuelle(DEFAULT_QUANTITE_CONTRACTUELLE)
            .quantiteRealisee(DEFAULT_QUANTITE_REALISEE);
        // Add required entity
        Affaire affaire;
        if (TestUtil.findAll(em, Affaire.class).isEmpty()) {
            affaire = AffaireResourceIT.createEntity(em);
            em.persist(affaire);
            em.flush();
        } else {
            affaire = TestUtil.findAll(em, Affaire.class).get(0);
        }
        affaireArticle.setAffaire(affaire);
        // Add required entity
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            article = ArticleResourceIT.createEntity(em);
            em.persist(article);
            em.flush();
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        affaireArticle.setArticle(article);
        return affaireArticle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffaireArticle createUpdatedEntity(EntityManager em) {
        AffaireArticle affaireArticle = new AffaireArticle()
            .quantiteContractuelle(UPDATED_QUANTITE_CONTRACTUELLE)
            .quantiteRealisee(UPDATED_QUANTITE_REALISEE);
        // Add required entity
        Affaire affaire;
        if (TestUtil.findAll(em, Affaire.class).isEmpty()) {
            affaire = AffaireResourceIT.createUpdatedEntity(em);
            em.persist(affaire);
            em.flush();
        } else {
            affaire = TestUtil.findAll(em, Affaire.class).get(0);
        }
        affaireArticle.setAffaire(affaire);
        // Add required entity
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            article = ArticleResourceIT.createUpdatedEntity(em);
            em.persist(article);
            em.flush();
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        affaireArticle.setArticle(article);
        return affaireArticle;
    }

    @BeforeEach
    public void initTest() {
        affaireArticle = createEntity(em);
    }

    @Test
    @Transactional
    void createAffaireArticle() throws Exception {
        int databaseSizeBeforeCreate = affaireArticleRepository.findAll().size();
        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);
        restAffaireArticleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeCreate + 1);
        AffaireArticle testAffaireArticle = affaireArticleList.get(affaireArticleList.size() - 1);
        assertThat(testAffaireArticle.getQuantiteContractuelle()).isEqualTo(DEFAULT_QUANTITE_CONTRACTUELLE);
        assertThat(testAffaireArticle.getQuantiteRealisee()).isEqualTo(DEFAULT_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void createAffaireArticleWithExistingId() throws Exception {
        // Create the AffaireArticle with an existing ID
        affaireArticle.setId(1L);
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        int databaseSizeBeforeCreate = affaireArticleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffaireArticleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAffaireArticles() throws Exception {
        // Initialize the database
        affaireArticleRepository.saveAndFlush(affaireArticle);

        // Get all the affaireArticleList
        restAffaireArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affaireArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantiteContractuelle").value(hasItem(DEFAULT_QUANTITE_CONTRACTUELLE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantiteRealisee").value(hasItem(DEFAULT_QUANTITE_REALISEE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAffaireArticlesWithEagerRelationshipsIsEnabled() throws Exception {
        when(affaireArticleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAffaireArticleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(affaireArticleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAffaireArticlesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(affaireArticleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAffaireArticleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(affaireArticleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAffaireArticle() throws Exception {
        // Initialize the database
        affaireArticleRepository.saveAndFlush(affaireArticle);

        // Get the affaireArticle
        restAffaireArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, affaireArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affaireArticle.getId().intValue()))
            .andExpect(jsonPath("$.quantiteContractuelle").value(DEFAULT_QUANTITE_CONTRACTUELLE.doubleValue()))
            .andExpect(jsonPath("$.quantiteRealisee").value(DEFAULT_QUANTITE_REALISEE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAffaireArticle() throws Exception {
        // Get the affaireArticle
        restAffaireArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAffaireArticle() throws Exception {
        // Initialize the database
        affaireArticleRepository.saveAndFlush(affaireArticle);

        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();

        // Update the affaireArticle
        AffaireArticle updatedAffaireArticle = affaireArticleRepository.findById(affaireArticle.getId()).get();
        // Disconnect from session so that the updates on updatedAffaireArticle are not directly saved in db
        em.detach(updatedAffaireArticle);
        updatedAffaireArticle.quantiteContractuelle(UPDATED_QUANTITE_CONTRACTUELLE).quantiteRealisee(UPDATED_QUANTITE_REALISEE);
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(updatedAffaireArticle);

        restAffaireArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaireArticleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isOk());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
        AffaireArticle testAffaireArticle = affaireArticleList.get(affaireArticleList.size() - 1);
        assertThat(testAffaireArticle.getQuantiteContractuelle()).isEqualTo(UPDATED_QUANTITE_CONTRACTUELLE);
        assertThat(testAffaireArticle.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void putNonExistingAffaireArticle() throws Exception {
        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();
        affaireArticle.setId(count.incrementAndGet());

        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affaireArticleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffaireArticle() throws Exception {
        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();
        affaireArticle.setId(count.incrementAndGet());

        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffaireArticle() throws Exception {
        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();
        affaireArticle.setId(count.incrementAndGet());

        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireArticleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffaireArticleWithPatch() throws Exception {
        // Initialize the database
        affaireArticleRepository.saveAndFlush(affaireArticle);

        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();

        // Update the affaireArticle using partial update
        AffaireArticle partialUpdatedAffaireArticle = new AffaireArticle();
        partialUpdatedAffaireArticle.setId(affaireArticle.getId());

        partialUpdatedAffaireArticle.quantiteContractuelle(UPDATED_QUANTITE_CONTRACTUELLE);

        restAffaireArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaireArticle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaireArticle))
            )
            .andExpect(status().isOk());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
        AffaireArticle testAffaireArticle = affaireArticleList.get(affaireArticleList.size() - 1);
        assertThat(testAffaireArticle.getQuantiteContractuelle()).isEqualTo(UPDATED_QUANTITE_CONTRACTUELLE);
        assertThat(testAffaireArticle.getQuantiteRealisee()).isEqualTo(DEFAULT_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void fullUpdateAffaireArticleWithPatch() throws Exception {
        // Initialize the database
        affaireArticleRepository.saveAndFlush(affaireArticle);

        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();

        // Update the affaireArticle using partial update
        AffaireArticle partialUpdatedAffaireArticle = new AffaireArticle();
        partialUpdatedAffaireArticle.setId(affaireArticle.getId());

        partialUpdatedAffaireArticle.quantiteContractuelle(UPDATED_QUANTITE_CONTRACTUELLE).quantiteRealisee(UPDATED_QUANTITE_REALISEE);

        restAffaireArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffaireArticle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffaireArticle))
            )
            .andExpect(status().isOk());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
        AffaireArticle testAffaireArticle = affaireArticleList.get(affaireArticleList.size() - 1);
        assertThat(testAffaireArticle.getQuantiteContractuelle()).isEqualTo(UPDATED_QUANTITE_CONTRACTUELLE);
        assertThat(testAffaireArticle.getQuantiteRealisee()).isEqualTo(UPDATED_QUANTITE_REALISEE);
    }

    @Test
    @Transactional
    void patchNonExistingAffaireArticle() throws Exception {
        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();
        affaireArticle.setId(count.incrementAndGet());

        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffaireArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affaireArticleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffaireArticle() throws Exception {
        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();
        affaireArticle.setId(count.incrementAndGet());

        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffaireArticle() throws Exception {
        int databaseSizeBeforeUpdate = affaireArticleRepository.findAll().size();
        affaireArticle.setId(count.incrementAndGet());

        // Create the AffaireArticle
        AffaireArticleDTO affaireArticleDTO = affaireArticleMapper.toDto(affaireArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffaireArticleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affaireArticleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffaireArticle in the database
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffaireArticle() throws Exception {
        // Initialize the database
        affaireArticleRepository.saveAndFlush(affaireArticle);

        int databaseSizeBeforeDelete = affaireArticleRepository.findAll().size();

        // Delete the affaireArticle
        restAffaireArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, affaireArticle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AffaireArticle> affaireArticleList = affaireArticleRepository.findAll();
        assertThat(affaireArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
