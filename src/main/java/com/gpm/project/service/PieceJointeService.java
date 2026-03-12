package com.gpm.project.service;

import com.gpm.project.domain.PieceJointe;
import com.gpm.project.repository.PieceJointeRepository;
import com.gpm.project.service.dto.PieceJointeDTO;
import com.gpm.project.service.mapper.PieceJointeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PieceJointe}.
 */
@Service
@Transactional
public class PieceJointeService {

    private final Logger log = LoggerFactory.getLogger(PieceJointeService.class);

    private final PieceJointeRepository pieceJointeRepository;

    private final PieceJointeMapper pieceJointeMapper;

    public PieceJointeService(PieceJointeRepository pieceJointeRepository, PieceJointeMapper pieceJointeMapper) {
        this.pieceJointeRepository = pieceJointeRepository;
        this.pieceJointeMapper = pieceJointeMapper;
    }

    /**
     * Save a pieceJointe.
     *
     * @param pieceJointeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJointeDTO save(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to save PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    /**
     * Update a pieceJointe.
     *
     * @param pieceJointeDTO the entity to save.
     * @return the persisted entity.
     */
    public PieceJointeDTO update(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to update PieceJointe : {}", pieceJointeDTO);
        PieceJointe pieceJointe = pieceJointeMapper.toEntity(pieceJointeDTO);
        pieceJointe = pieceJointeRepository.save(pieceJointe);
        return pieceJointeMapper.toDto(pieceJointe);
    }

    /**
     * Partially update a pieceJointe.
     *
     * @param pieceJointeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PieceJointeDTO> partialUpdate(PieceJointeDTO pieceJointeDTO) {
        log.debug("Request to partially update PieceJointe : {}", pieceJointeDTO);

        return pieceJointeRepository
            .findById(pieceJointeDTO.getId())
            .map(existingPieceJointe -> {
                pieceJointeMapper.partialUpdate(existingPieceJointe, pieceJointeDTO);

                return existingPieceJointe;
            })
            .map(pieceJointeRepository::save)
            .map(pieceJointeMapper::toDto);
    }

    /**
     * Get all the pieceJointes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PieceJointeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PieceJointes");
        return pieceJointeRepository.findAll(pageable).map(pieceJointeMapper::toDto);
    }

    /**
     * Get all the pieceJointes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PieceJointeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pieceJointeRepository.findAllWithEagerRelationships(pageable).map(pieceJointeMapper::toDto);
    }

    /**
     * Get one pieceJointe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PieceJointeDTO> findOne(Long id) {
        log.debug("Request to get PieceJointe : {}", id);
        return pieceJointeRepository.findOneWithEagerRelationships(id).map(pieceJointeMapper::toDto);
    }

    /**
     * Delete the pieceJointe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PieceJointe : {}", id);
        pieceJointeRepository.deleteById(id);
    }
}
