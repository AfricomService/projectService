package com.gpm.project.service;

import com.gpm.project.domain.JourFerie;
import com.gpm.project.repository.JourFerieRepository;
import com.gpm.project.service.dto.JourFerieDTO;
import com.gpm.project.service.mapper.JourFerieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link JourFerie}.
 */
@Service
@Transactional
public class JourFerieService {

    private final Logger log = LoggerFactory.getLogger(JourFerieService.class);

    private final JourFerieRepository jourFerieRepository;

    private final JourFerieMapper jourFerieMapper;

    public JourFerieService(JourFerieRepository jourFerieRepository, JourFerieMapper jourFerieMapper) {
        this.jourFerieRepository = jourFerieRepository;
        this.jourFerieMapper = jourFerieMapper;
    }

    /**
     * Save a jourFerie.
     *
     * @param jourFerieDTO the entity to save.
     * @return the persisted entity.
     */
    public JourFerieDTO save(JourFerieDTO jourFerieDTO) {
        log.debug("Request to save JourFerie : {}", jourFerieDTO);
        JourFerie jourFerie = jourFerieMapper.toEntity(jourFerieDTO);
        jourFerie = jourFerieRepository.save(jourFerie);
        return jourFerieMapper.toDto(jourFerie);
    }

    /**
     * Update a jourFerie.
     *
     * @param jourFerieDTO the entity to save.
     * @return the persisted entity.
     */
    public JourFerieDTO update(JourFerieDTO jourFerieDTO) {
        log.debug("Request to update JourFerie : {}", jourFerieDTO);
        JourFerie jourFerie = jourFerieMapper.toEntity(jourFerieDTO);
        jourFerie = jourFerieRepository.save(jourFerie);
        return jourFerieMapper.toDto(jourFerie);
    }

    /**
     * Partially update a jourFerie.
     *
     * @param jourFerieDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JourFerieDTO> partialUpdate(JourFerieDTO jourFerieDTO) {
        log.debug("Request to partially update JourFerie : {}", jourFerieDTO);

        return jourFerieRepository
            .findById(jourFerieDTO.getId())
            .map(existingJourFerie -> {
                jourFerieMapper.partialUpdate(existingJourFerie, jourFerieDTO);

                return existingJourFerie;
            })
            .map(jourFerieRepository::save)
            .map(jourFerieMapper::toDto);
    }

    /**
     * Get all the jourFeries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JourFerieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JourFeries");
        return jourFerieRepository.findAll(pageable).map(jourFerieMapper::toDto);
    }

    /**
     * Get one jourFerie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JourFerieDTO> findOne(Long id) {
        log.debug("Request to get JourFerie : {}", id);
        return jourFerieRepository.findById(id).map(jourFerieMapper::toDto);
    }

    /**
     * Delete the jourFerie by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete JourFerie : {}", id);
        jourFerieRepository.deleteById(id);
    }
}
