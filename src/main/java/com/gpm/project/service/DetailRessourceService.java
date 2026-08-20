package com.gpm.project.service;

import com.gpm.project.domain.DetailRessource;
import com.gpm.project.repository.DetailRessourceRepository;
import com.gpm.project.service.dto.DetailRessourceDTO;
import com.gpm.project.service.mapper.DetailRessourceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetailRessource}.
 */
@Service
@Transactional
public class DetailRessourceService {

    private final Logger log = LoggerFactory.getLogger(DetailRessourceService.class);

    private final DetailRessourceRepository detailRessourceRepository;

    private final DetailRessourceMapper detailRessourceMapper;

    public DetailRessourceService(DetailRessourceRepository detailRessourceRepository, DetailRessourceMapper detailRessourceMapper) {
        this.detailRessourceRepository = detailRessourceRepository;
        this.detailRessourceMapper = detailRessourceMapper;
    }

    /**
     * Save a detailRessource.
     *
     * @param detailRessourceDTO the entity to save.
     * @return the persisted entity.
     */
    public DetailRessourceDTO save(DetailRessourceDTO detailRessourceDTO) {
        log.debug("Request to save DetailRessource : {}", detailRessourceDTO);
        DetailRessource detailRessource = detailRessourceMapper.toEntity(detailRessourceDTO);
        detailRessource = detailRessourceRepository.save(detailRessource);
        return detailRessourceMapper.toDto(detailRessource);
    }

    /**
     * Partially update a detailRessource.
     *
     * @param detailRessourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DetailRessourceDTO> partialUpdate(DetailRessourceDTO detailRessourceDTO) {
        log.debug("Request to partially update DetailRessource : {}", detailRessourceDTO);

        return detailRessourceRepository
            .findById(detailRessourceDTO.getId())
            .map(existingDetailRessource -> {
                detailRessourceMapper.partialUpdate(existingDetailRessource, detailRessourceDTO);

                return existingDetailRessource;
            })
            .map(detailRessourceRepository::save)
            .map(detailRessourceMapper::toDto);
    }

    /**
     * Get all the detailRessources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DetailRessourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DetailRessources");
        return detailRessourceRepository.findAll(pageable).map(detailRessourceMapper::toDto);
    }

    /**
     * Get one detailRessource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetailRessourceDTO> findOne(Long id) {
        log.debug("Request to get DetailRessource : {}", id);
        return detailRessourceRepository.findById(id).map(detailRessourceMapper::toDto);
    }

    /**
     * Delete the detailRessource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DetailRessource : {}", id);
        detailRessourceRepository.deleteById(id);
    }
}
