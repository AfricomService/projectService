package com.gpm.project.service;

import com.gpm.project.domain.AffaireSocieteAdj;
import com.gpm.project.repository.AffaireSocieteAdjRepository;
import com.gpm.project.service.dto.AffaireSocieteAdjDTO;
import com.gpm.project.service.mapper.AffaireSocieteAdjMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AffaireSocieteAdj}.
 */
@Service
@Transactional
public class AffaireSocieteAdjService {

    private final Logger log = LoggerFactory.getLogger(AffaireSocieteAdjService.class);

    private final AffaireSocieteAdjRepository affaireSocieteAdjRepository;

    private final AffaireSocieteAdjMapper affaireSocieteAdjMapper;

    public AffaireSocieteAdjService(
        AffaireSocieteAdjRepository affaireSocieteAdjRepository,
        AffaireSocieteAdjMapper affaireSocieteAdjMapper
    ) {
        this.affaireSocieteAdjRepository = affaireSocieteAdjRepository;
        this.affaireSocieteAdjMapper = affaireSocieteAdjMapper;
    }

    /**
     * Save a affaireSocieteAdj.
     *
     * @param affaireSocieteAdjDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireSocieteAdjDTO save(AffaireSocieteAdjDTO affaireSocieteAdjDTO) {
        log.debug("Request to save AffaireSocieteAdj : {}", affaireSocieteAdjDTO);
        AffaireSocieteAdj affaireSocieteAdj = affaireSocieteAdjMapper.toEntity(affaireSocieteAdjDTO);
        affaireSocieteAdj = affaireSocieteAdjRepository.save(affaireSocieteAdj);
        return affaireSocieteAdjMapper.toDto(affaireSocieteAdj);
    }

    /**
     * Update a affaireSocieteAdj.
     *
     * @param affaireSocieteAdjDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireSocieteAdjDTO update(AffaireSocieteAdjDTO affaireSocieteAdjDTO) {
        log.debug("Request to update AffaireSocieteAdj : {}", affaireSocieteAdjDTO);
        AffaireSocieteAdj affaireSocieteAdj = affaireSocieteAdjMapper.toEntity(affaireSocieteAdjDTO);
        affaireSocieteAdj = affaireSocieteAdjRepository.save(affaireSocieteAdj);
        return affaireSocieteAdjMapper.toDto(affaireSocieteAdj);
    }

    /**
     * Partially update a affaireSocieteAdj.
     *
     * @param affaireSocieteAdjDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AffaireSocieteAdjDTO> partialUpdate(AffaireSocieteAdjDTO affaireSocieteAdjDTO) {
        log.debug("Request to partially update AffaireSocieteAdj : {}", affaireSocieteAdjDTO);

        return affaireSocieteAdjRepository
            .findById(affaireSocieteAdjDTO.getId())
            .map(existingAffaireSocieteAdj -> {
                affaireSocieteAdjMapper.partialUpdate(existingAffaireSocieteAdj, affaireSocieteAdjDTO);

                return existingAffaireSocieteAdj;
            })
            .map(affaireSocieteAdjRepository::save)
            .map(affaireSocieteAdjMapper::toDto);
    }

    /**
     * Get all the affaireSocieteAdjs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AffaireSocieteAdjDTO> findAll() {
        log.debug("Request to get all AffaireSocieteAdjs");
        return affaireSocieteAdjRepository
            .findAll()
            .stream()
            .map(affaireSocieteAdjMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one affaireSocieteAdj by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AffaireSocieteAdjDTO> findOne(Long id) {
        log.debug("Request to get AffaireSocieteAdj : {}", id);
        return affaireSocieteAdjRepository.findById(id).map(affaireSocieteAdjMapper::toDto);
    }

    /**
     * Delete the affaireSocieteAdj by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AffaireSocieteAdj : {}", id);
        affaireSocieteAdjRepository.deleteById(id);
    }
}
