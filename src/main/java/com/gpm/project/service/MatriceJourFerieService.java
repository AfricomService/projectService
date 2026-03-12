package com.gpm.project.service;

import com.gpm.project.domain.MatriceJourFerie;
import com.gpm.project.repository.MatriceJourFerieRepository;
import com.gpm.project.service.dto.MatriceJourFerieDTO;
import com.gpm.project.service.mapper.MatriceJourFerieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MatriceJourFerie}.
 */
@Service
@Transactional
public class MatriceJourFerieService {

    private final Logger log = LoggerFactory.getLogger(MatriceJourFerieService.class);

    private final MatriceJourFerieRepository matriceJourFerieRepository;

    private final MatriceJourFerieMapper matriceJourFerieMapper;

    public MatriceJourFerieService(MatriceJourFerieRepository matriceJourFerieRepository, MatriceJourFerieMapper matriceJourFerieMapper) {
        this.matriceJourFerieRepository = matriceJourFerieRepository;
        this.matriceJourFerieMapper = matriceJourFerieMapper;
    }

    /**
     * Save a matriceJourFerie.
     *
     * @param matriceJourFerieDTO the entity to save.
     * @return the persisted entity.
     */
    public MatriceJourFerieDTO save(MatriceJourFerieDTO matriceJourFerieDTO) {
        log.debug("Request to save MatriceJourFerie : {}", matriceJourFerieDTO);
        MatriceJourFerie matriceJourFerie = matriceJourFerieMapper.toEntity(matriceJourFerieDTO);
        matriceJourFerie = matriceJourFerieRepository.save(matriceJourFerie);
        return matriceJourFerieMapper.toDto(matriceJourFerie);
    }

    /**
     * Update a matriceJourFerie.
     *
     * @param matriceJourFerieDTO the entity to save.
     * @return the persisted entity.
     */
    public MatriceJourFerieDTO update(MatriceJourFerieDTO matriceJourFerieDTO) {
        log.debug("Request to update MatriceJourFerie : {}", matriceJourFerieDTO);
        MatriceJourFerie matriceJourFerie = matriceJourFerieMapper.toEntity(matriceJourFerieDTO);
        matriceJourFerie = matriceJourFerieRepository.save(matriceJourFerie);
        return matriceJourFerieMapper.toDto(matriceJourFerie);
    }

    /**
     * Partially update a matriceJourFerie.
     *
     * @param matriceJourFerieDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatriceJourFerieDTO> partialUpdate(MatriceJourFerieDTO matriceJourFerieDTO) {
        log.debug("Request to partially update MatriceJourFerie : {}", matriceJourFerieDTO);

        return matriceJourFerieRepository
            .findById(matriceJourFerieDTO.getId())
            .map(existingMatriceJourFerie -> {
                matriceJourFerieMapper.partialUpdate(existingMatriceJourFerie, matriceJourFerieDTO);

                return existingMatriceJourFerie;
            })
            .map(matriceJourFerieRepository::save)
            .map(matriceJourFerieMapper::toDto);
    }

    /**
     * Get all the matriceJourFeries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MatriceJourFerieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MatriceJourFeries");
        return matriceJourFerieRepository.findAll(pageable).map(matriceJourFerieMapper::toDto);
    }

    /**
     * Get all the matriceJourFeries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MatriceJourFerieDTO> findAllWithEagerRelationships(Pageable pageable) {
        return matriceJourFerieRepository.findAllWithEagerRelationships(pageable).map(matriceJourFerieMapper::toDto);
    }

    /**
     * Get one matriceJourFerie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatriceJourFerieDTO> findOne(Long id) {
        log.debug("Request to get MatriceJourFerie : {}", id);
        return matriceJourFerieRepository.findOneWithEagerRelationships(id).map(matriceJourFerieMapper::toDto);
    }

    /**
     * Delete the matriceJourFerie by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MatriceJourFerie : {}", id);
        matriceJourFerieRepository.deleteById(id);
    }
}
