package com.gpm.project.service;

import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.AffaireArticle;
import com.gpm.project.domain.Article;
import com.gpm.project.repository.AffaireArticleRepository;
import com.gpm.project.repository.AffaireRepository;
import com.gpm.project.repository.ArticleRepository;
import com.gpm.project.service.dto.AffaireArticleDTO;
import com.gpm.project.service.mapper.AffaireArticleMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AffaireArticle}.
 */
@Service
@Transactional
public class AffaireArticleService {

    private final Logger log = LoggerFactory.getLogger(AffaireArticleService.class);

    private final AffaireArticleRepository affaireArticleRepository;
    private final ArticleRepository articleRepository;
    private final AffaireRepository affaireRepository;
    private final AffaireArticleMapper affaireArticleMapper;

    public AffaireArticleService(
        AffaireArticleRepository affaireArticleRepository,
        ArticleRepository articleRepository,
        AffaireRepository affaireRepository,
        AffaireArticleMapper affaireArticleMapper
    ) {
        this.affaireArticleRepository = affaireArticleRepository;
        this.articleRepository = articleRepository;
        this.affaireRepository = affaireRepository;
        this.affaireArticleMapper = affaireArticleMapper;
    }

    @Transactional(readOnly = true)
    public Page<Article> getArticlesByAffaire(Long affaireId, String searchTerm, Pageable pageable) {
        return articleRepository.findArticlesByAffaireIdFiltered(affaireId, searchTerm, pageable);
    }

    public void removeRelation(Long affaireId, Long articleId) {
        affaireArticleRepository.deleteByAffaireIdAndArticleId(affaireId, articleId);
    }

    public void replaceArticlesForAffaire(Long affaireId, List<Long> articleIds) {
        // 1. Delete old relations
        affaireArticleRepository.deleteByAffaireId(affaireId);

        // 2. Fetch the Affaire
        Affaire affaire = affaireRepository
            .findById(affaireId)
            .orElseThrow(() -> new EntityNotFoundException("Affaire not found with ID: " + affaireId));

        // 3. Create new relations
        List<AffaireArticle> newRelations = articleIds
            .stream()
            .map(articleId -> {
                Article article = articleRepository
                    .findById(articleId)
                    .orElseThrow(() -> new EntityNotFoundException("Article not found with ID: " + articleId));

                AffaireArticle affaireArticle = new AffaireArticle();
                affaireArticle.setAffaire(affaire);
                affaireArticle.setArticle(article);
                // Note: quantiteContractuelle and quantiteRealisee will be null.

                return affaireArticle;
            })
            .collect(Collectors.toList());

        // 4. Save new relations
        affaireArticleRepository.saveAll(newRelations);
    }

    /**
     * Save a affaireArticle.
     *
     * @param affaireArticleDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireArticleDTO save(AffaireArticleDTO affaireArticleDTO) {
        log.debug("Request to save AffaireArticle : {}", affaireArticleDTO);
        AffaireArticle affaireArticle = affaireArticleMapper.toEntity(affaireArticleDTO);
        affaireArticle = affaireArticleRepository.save(affaireArticle);
        return affaireArticleMapper.toDto(affaireArticle);
    }

    /**
     * Update a affaireArticle.
     *
     * @param affaireArticleDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireArticleDTO update(AffaireArticleDTO affaireArticleDTO) {
        log.debug("Request to update AffaireArticle : {}", affaireArticleDTO);
        AffaireArticle affaireArticle = affaireArticleMapper.toEntity(affaireArticleDTO);
        affaireArticle = affaireArticleRepository.save(affaireArticle);
        return affaireArticleMapper.toDto(affaireArticle);
    }

    /**
     * Partially update a affaireArticle.
     *
     * @param affaireArticleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AffaireArticleDTO> partialUpdate(AffaireArticleDTO affaireArticleDTO) {
        log.debug("Request to partially update AffaireArticle : {}", affaireArticleDTO);

        return affaireArticleRepository
            .findById(affaireArticleDTO.getId())
            .map(existingAffaireArticle -> {
                affaireArticleMapper.partialUpdate(existingAffaireArticle, affaireArticleDTO);

                return existingAffaireArticle;
            })
            .map(affaireArticleRepository::save)
            .map(affaireArticleMapper::toDto);
    }

    /**
     * Get all the affaireArticles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AffaireArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AffaireArticles");
        return affaireArticleRepository.findAll(pageable).map(affaireArticleMapper::toDto);
    }

    /**
     * Get all the affaireArticles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AffaireArticleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return affaireArticleRepository.findAllWithEagerRelationships(pageable).map(affaireArticleMapper::toDto);
    }

    /**
     * Get one affaireArticle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AffaireArticleDTO> findOne(Long id) {
        log.debug("Request to get AffaireArticle : {}", id);
        return affaireArticleRepository.findOneWithEagerRelationships(id).map(affaireArticleMapper::toDto);
    }

    /**
     * Delete the affaireArticle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AffaireArticle : {}", id);
        affaireArticleRepository.deleteById(id);
    }

    /**
     * Get all the affaireArticles by affaire id.
     *
     * @param affaireId the id of the affaire.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public java.util.List<AffaireArticleDTO> findByAffaireId(Long affaireId) {
        log.debug("Request to get AffaireArticles by affaireId : {}", affaireId);
        return affaireArticleRepository
            .findByAffaireId(affaireId)
            .stream()
            .map(affaireArticleMapper::toDto)
            .collect(java.util.stream.Collectors.toList());
    }
}
