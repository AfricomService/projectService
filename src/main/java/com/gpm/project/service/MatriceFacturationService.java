package com.gpm.project.service;

import com.gpm.project.domain.MatriceFacturation;
import com.gpm.project.repository.MatriceFacturationRepository;
import com.gpm.project.service.dto.MatriceFacturationDTO;
import com.gpm.project.service.mapper.MatriceFacturationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MatriceFacturation}.
 */
@Service
@Transactional
public class MatriceFacturationService {

    private final Logger log = LoggerFactory.getLogger(MatriceFacturationService.class);

    private final MatriceFacturationRepository matriceFacturationRepository;

    private final MatriceFacturationMapper matriceFacturationMapper;

    public MatriceFacturationService(
        MatriceFacturationRepository matriceFacturationRepository,
        MatriceFacturationMapper matriceFacturationMapper
    ) {
        this.matriceFacturationRepository = matriceFacturationRepository;
        this.matriceFacturationMapper = matriceFacturationMapper;
    }

    /**
     * Save a matriceFacturation.
     *
     * @param matriceFacturationDTO the entity to save.
     * @return the persisted entity.
     */
    public MatriceFacturationDTO save(MatriceFacturationDTO matriceFacturationDTO) {
        log.debug("Request to save MatriceFacturation : {}", matriceFacturationDTO);
        MatriceFacturation matriceFacturation = matriceFacturationMapper.toEntity(matriceFacturationDTO);
        matriceFacturation = matriceFacturationRepository.save(matriceFacturation);
        return matriceFacturationMapper.toDto(matriceFacturation);
    }

    /**
     * Update a matriceFacturation.
     *
     * @param matriceFacturationDTO the entity to save.
     * @return the persisted entity.
     */
    public MatriceFacturationDTO update(MatriceFacturationDTO matriceFacturationDTO) {
        log.debug("Request to update MatriceFacturation : {}", matriceFacturationDTO);
        MatriceFacturation matriceFacturation = matriceFacturationMapper.toEntity(matriceFacturationDTO);
        matriceFacturation = matriceFacturationRepository.save(matriceFacturation);
        return matriceFacturationMapper.toDto(matriceFacturation);
    }

    /**
     * Partially update a matriceFacturation.
     *
     * @param matriceFacturationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatriceFacturationDTO> partialUpdate(MatriceFacturationDTO matriceFacturationDTO) {
        log.debug("Request to partially update MatriceFacturation : {}", matriceFacturationDTO);

        return matriceFacturationRepository
            .findById(matriceFacturationDTO.getId())
            .map(existingMatriceFacturation -> {
                matriceFacturationMapper.partialUpdate(existingMatriceFacturation, matriceFacturationDTO);

                return existingMatriceFacturation;
            })
            .map(matriceFacturationRepository::save)
            .map(matriceFacturationMapper::toDto);
    }

    /**
     * Get all the matriceFacturations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatriceFacturationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MatriceFacturations");
        return matriceFacturationRepository.findAll(pageable).map(matriceFacturationMapper::toDto);
    }

    /**
     * Get all the matriceFacturations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MatriceFacturationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matriceFacturationRepository.findAllWithEagerRelationships(pageable).map(matriceFacturationMapper::toDto);
    }

    /**
     * Get one matriceFacturation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatriceFacturationDTO> findOne(Long id) {
        log.debug("Request to get MatriceFacturation : {}", id);
        return matriceFacturationRepository.findOneWithEagerRelationships(id).map(matriceFacturationMapper::toDto);
    }

    /**
     * Get matrice facturations by affaire id.
     *
     * @param affaireId the id of the affaire.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public java.util.List<MatriceFacturationDTO> findMatriceByAffaireId(Long affaireId) {
        log.debug("Request to get MatriceFacturation by affaire id : {}", affaireId);
        return matriceFacturationRepository
            .findAllByAffaireId(affaireId)
            .stream()
            .map(matriceFacturationMapper::toDto)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Delete the matriceFacturation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MatriceFacturation : {}", id);
        matriceFacturationRepository.deleteById(id);
    }
}
