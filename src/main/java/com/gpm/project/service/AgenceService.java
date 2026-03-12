package com.gpm.project.service;

import com.gpm.project.domain.Agence;
import com.gpm.project.repository.AgenceRepository;
import com.gpm.project.service.dto.AgenceDTO;
import com.gpm.project.service.mapper.AgenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Agence}.
 */
@Service
@Transactional
public class AgenceService {

    private final Logger log = LoggerFactory.getLogger(AgenceService.class);

    private final AgenceRepository agenceRepository;

    private final AgenceMapper agenceMapper;

    public AgenceService(AgenceRepository agenceRepository, AgenceMapper agenceMapper) {
        this.agenceRepository = agenceRepository;
        this.agenceMapper = agenceMapper;
    }

    /**
     * Save a agence.
     *
     * @param agenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AgenceDTO save(AgenceDTO agenceDTO) {
        log.debug("Request to save Agence : {}", agenceDTO);
        Agence agence = agenceMapper.toEntity(agenceDTO);
        agence = agenceRepository.save(agence);
        return agenceMapper.toDto(agence);
    }

    /**
     * Update a agence.
     *
     * @param agenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AgenceDTO update(AgenceDTO agenceDTO) {
        log.debug("Request to update Agence : {}", agenceDTO);
        Agence agence = agenceMapper.toEntity(agenceDTO);
        agence = agenceRepository.save(agence);
        return agenceMapper.toDto(agence);
    }

    /**
     * Partially update a agence.
     *
     * @param agenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgenceDTO> partialUpdate(AgenceDTO agenceDTO) {
        log.debug("Request to partially update Agence : {}", agenceDTO);

        return agenceRepository
            .findById(agenceDTO.getId())
            .map(existingAgence -> {
                agenceMapper.partialUpdate(existingAgence, agenceDTO);

                return existingAgence;
            })
            .map(agenceRepository::save)
            .map(agenceMapper::toDto);
    }

    /**
     * Get all the agences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AgenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agences");
        return agenceRepository.findAll(pageable).map(agenceMapper::toDto);
    }

    /**
     * Get all the agences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AgenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agenceRepository.findAllWithEagerRelationships(pageable).map(agenceMapper::toDto);
    }

    /**
     * Get one agence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgenceDTO> findOne(Long id) {
        log.debug("Request to get Agence : {}", id);
        return agenceRepository.findOneWithEagerRelationships(id).map(agenceMapper::toDto);
    }

    /**
     * Delete the agence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Agence : {}", id);
        agenceRepository.deleteById(id);
    }
}
