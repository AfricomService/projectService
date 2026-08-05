package com.gpm.project.service;

import com.gpm.project.domain.RoleContactSociete;
import com.gpm.project.repository.RoleContactSocieteRepository;
import com.gpm.project.service.dto.RoleContactSocieteDTO;
import com.gpm.project.service.mapper.RoleContactSocieteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoleContactSociete}.
 */
@Service
@Transactional
public class RoleContactSocieteService {

    private final Logger log = LoggerFactory.getLogger(RoleContactSocieteService.class);

    private final RoleContactSocieteRepository roleContactSocieteRepository;

    private final RoleContactSocieteMapper roleContactSocieteMapper;

    public RoleContactSocieteService(
        RoleContactSocieteRepository roleContactSocieteRepository,
        RoleContactSocieteMapper roleContactSocieteMapper
    ) {
        this.roleContactSocieteRepository = roleContactSocieteRepository;
        this.roleContactSocieteMapper = roleContactSocieteMapper;
    }

    /**
     * Save a roleContactSociete.
     *
     * @param roleContactSocieteDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleContactSocieteDTO save(RoleContactSocieteDTO roleContactSocieteDTO) {
        log.debug("Request to save RoleContactSociete : {}", roleContactSocieteDTO);
        RoleContactSociete roleContactSociete = roleContactSocieteMapper.toEntity(roleContactSocieteDTO);
        roleContactSociete = roleContactSocieteRepository.save(roleContactSociete);
        return roleContactSocieteMapper.toDto(roleContactSociete);
    }

    /**
     * Update a roleContactSociete.
     *
     * @param roleContactSocieteDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleContactSocieteDTO update(RoleContactSocieteDTO roleContactSocieteDTO) {
        log.debug("Request to update RoleContactSociete : {}", roleContactSocieteDTO);
        RoleContactSociete roleContactSociete = roleContactSocieteMapper.toEntity(roleContactSocieteDTO);
        roleContactSociete = roleContactSocieteRepository.save(roleContactSociete);
        return roleContactSocieteMapper.toDto(roleContactSociete);
    }

    /**
     * Partially update a roleContactSociete.
     *
     * @param roleContactSocieteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoleContactSocieteDTO> partialUpdate(RoleContactSocieteDTO roleContactSocieteDTO) {
        log.debug("Request to partially update RoleContactSociete : {}", roleContactSocieteDTO);

        return roleContactSocieteRepository
            .findById(roleContactSocieteDTO.getId())
            .map(existingRoleContactSociete -> {
                roleContactSocieteMapper.partialUpdate(existingRoleContactSociete, roleContactSocieteDTO);

                return existingRoleContactSociete;
            })
            .map(roleContactSocieteRepository::save)
            .map(roleContactSocieteMapper::toDto);
    }

    /**
     * Get all the roleContactSocietes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoleContactSocieteDTO> findAll() {
        log.debug("Request to get all RoleContactSocietes");
        return roleContactSocieteRepository
            .findAll()
            .stream()
            .map(roleContactSocieteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one roleContactSociete by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleContactSocieteDTO> findOne(Long id) {
        log.debug("Request to get RoleContactSociete : {}", id);
        return roleContactSocieteRepository.findById(id).map(roleContactSocieteMapper::toDto);
    }

    /**
     * Delete the roleContactSociete by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoleContactSociete : {}", id);
        roleContactSocieteRepository.deleteById(id);
    }
}
