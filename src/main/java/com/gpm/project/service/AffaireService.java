package com.gpm.project.service;

import com.gpm.project.domain.Affaire;
import com.gpm.project.repository.AffaireRepository;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.mapper.AffaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Affaire}.
 */
@Service
@Transactional
public class AffaireService {

    private final Logger log = LoggerFactory.getLogger(AffaireService.class);

    private final AffaireRepository affaireRepository;

    private final AffaireMapper affaireMapper;

    public AffaireService(AffaireRepository affaireRepository, AffaireMapper affaireMapper) {
        this.affaireRepository = affaireRepository;
        this.affaireMapper = affaireMapper;
    }

    /**
     * Save a affaire.
     *
     * @param affaireDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireDTO save(AffaireDTO affaireDTO) {
        log.debug("Request to save Affaire : {}", affaireDTO);
        Affaire affaire = affaireMapper.toEntity(affaireDTO);
        affaire = affaireRepository.save(affaire);
        return affaireMapper.toDto(affaire);
    }

    /**
     * Update a affaire.
     *
     * @param affaireDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireDTO update(AffaireDTO affaireDTO) {
        log.debug("Request to update Affaire : {}", affaireDTO);
        Affaire affaire = affaireMapper.toEntity(affaireDTO);
        affaire = affaireRepository.save(affaire);
        return affaireMapper.toDto(affaire);
    }

    /**
     * Partially update a affaire.
     *
     * @param affaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AffaireDTO> partialUpdate(AffaireDTO affaireDTO) {
        log.debug("Request to partially update Affaire : {}", affaireDTO);

        return affaireRepository
            .findById(affaireDTO.getId())
            .map(existingAffaire -> {
                affaireMapper.partialUpdate(existingAffaire, affaireDTO);

                return existingAffaire;
            })
            .map(affaireRepository::save)
            .map(affaireMapper::toDto);
    }

    /**
     * Get all the affaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AffaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Affaires");
        return affaireRepository.findAll(pageable).map(affaireMapper::toDto);
    }

    /**
     * Get all the affaires with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AffaireDTO> findAllWithEagerRelationships(Pageable pageable) {
        return affaireRepository.findAllWithEagerRelationships(pageable).map(affaireMapper::toDto);
    }

    /**
     * Get one affaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AffaireDTO> findOne(Long id) {
        log.debug("Request to get Affaire : {}", id);
        return affaireRepository.findOneWithEagerRelationships(id).map(affaireMapper::toDto);
    }

    /**
     * Delete the affaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Affaire : {}", id);
        affaireRepository.deleteById(id);
    }
}
