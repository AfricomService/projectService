package com.gpm.project.service;

import com.gpm.project.domain.Vehicule;
import com.gpm.project.repository.VehiculeRepository;
import com.gpm.project.service.dto.VehiculeDTO;
import com.gpm.project.service.mapper.VehiculeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vehicule}.
 */
@Service
@Transactional
public class VehiculeService {

    private final Logger log = LoggerFactory.getLogger(VehiculeService.class);

    private final VehiculeRepository vehiculeRepository;

    private final VehiculeMapper vehiculeMapper;

    public VehiculeService(VehiculeRepository vehiculeRepository, VehiculeMapper vehiculeMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.vehiculeMapper = vehiculeMapper;
    }

    /**
     * Save a vehicule.
     *
     * @param vehiculeDTO the entity to save.
     * @return the persisted entity.
     */
    public VehiculeDTO save(VehiculeDTO vehiculeDTO) {
        log.debug("Request to save Vehicule : {}", vehiculeDTO);
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);
        vehicule = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(vehicule);
    }

    /**
     * Update a vehicule.
     *
     * @param vehiculeDTO the entity to save.
     * @return the persisted entity.
     */
    public VehiculeDTO update(VehiculeDTO vehiculeDTO) {
        log.debug("Request to update Vehicule : {}", vehiculeDTO);
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);
        vehicule = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(vehicule);
    }

    /**
     * Partially update a vehicule.
     *
     * @param vehiculeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehiculeDTO> partialUpdate(VehiculeDTO vehiculeDTO) {
        log.debug("Request to partially update Vehicule : {}", vehiculeDTO);

        return vehiculeRepository
            .findById(vehiculeDTO.getId())
            .map(existingVehicule -> {
                vehiculeMapper.partialUpdate(existingVehicule, vehiculeDTO);

                return existingVehicule;
            })
            .map(vehiculeRepository::save)
            .map(vehiculeMapper::toDto);
    }

    /**
     * Get all the vehicules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehiculeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vehicules");
        return vehiculeRepository.findAll(pageable).map(vehiculeMapper::toDto);
    }

    /**
     * Get all the vehicules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VehiculeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vehiculeRepository.findAllWithEagerRelationships(pageable).map(vehiculeMapper::toDto);
    }

    /**
     * Get one vehicule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehiculeDTO> findOne(Long id) {
        log.debug("Request to get Vehicule : {}", id);
        return vehiculeRepository.findOneWithEagerRelationships(id).map(vehiculeMapper::toDto);
    }

    /**
     * Delete the vehicule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vehicule : {}", id);
        vehiculeRepository.deleteById(id);
    }
}
