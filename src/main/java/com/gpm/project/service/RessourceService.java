package com.gpm.project.service;

import com.gpm.project.domain.Ressource;
import com.gpm.project.repository.RessourceRepository;
import com.gpm.project.service.dto.RessourceDTO;
import com.gpm.project.service.mapper.RessourceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ressource}.
 */
@Service
@Transactional
public class RessourceService {

    private final Logger log = LoggerFactory.getLogger(RessourceService.class);

    private final RessourceRepository ressourceRepository;

    private final RessourceMapper ressourceMapper;

    public RessourceService(RessourceRepository ressourceRepository, RessourceMapper ressourceMapper) {
        this.ressourceRepository = ressourceRepository;
        this.ressourceMapper = ressourceMapper;
    }

    /**
     * Save a ressource.
     *
     * @param ressourceDTO the entity to save.
     * @return the persisted entity.
     */
    public RessourceDTO save(RessourceDTO ressourceDTO) {
        log.debug("Request to save Ressource : {}", ressourceDTO);
        Ressource ressource = ressourceMapper.toEntity(ressourceDTO);
        ressource = ressourceRepository.save(ressource);
        return ressourceMapper.toDto(ressource);
    }

    /**
     * Partially update a ressource.
     *
     * @param ressourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RessourceDTO> partialUpdate(RessourceDTO ressourceDTO) {
        log.debug("Request to partially update Ressource : {}", ressourceDTO);

        return ressourceRepository
            .findById(ressourceDTO.getId())
            .map(existingRessource -> {
                ressourceMapper.partialUpdate(existingRessource, ressourceDTO);

                return existingRessource;
            })
            .map(ressourceRepository::save)
            .map(ressourceMapper::toDto);
    }

    /**
     * Get all the ressources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RessourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ressources");
        return ressourceRepository.findAll(pageable).map(ressourceMapper::toDto);
    }

    /**
     * Get one ressource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RessourceDTO> findOne(Long id) {
        log.debug("Request to get Ressource : {}", id);
        return ressourceRepository.findById(id).map(ressourceMapper::toDto);
    }

    /**
     * Delete the ressource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ressource : {}", id);
        ressourceRepository.deleteById(id);
    }
}
