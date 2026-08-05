package com.gpm.project.service;

import com.gpm.project.domain.UserAuthSociete;
import com.gpm.project.repository.UserAuthSocieteRepository;
import com.gpm.project.service.dto.AssignRoleDTO;
import com.gpm.project.service.dto.UserAuthSocieteDTO;
import com.gpm.project.service.mapper.UserAuthSocieteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserAuthSociete}.
 */
@Service
@Transactional
public class UserAuthSocieteService {

    private final Logger log = LoggerFactory.getLogger(UserAuthSocieteService.class);

    private final UserAuthSocieteRepository userAuthSocieteRepository;

    private final UserAuthSocieteMapper userAuthSocieteMapper;

    public UserAuthSocieteService(UserAuthSocieteRepository userAuthSocieteRepository, UserAuthSocieteMapper userAuthSocieteMapper) {
        this.userAuthSocieteRepository = userAuthSocieteRepository;
        this.userAuthSocieteMapper = userAuthSocieteMapper;
    }

    /**
     * Save a userAuthSociete.
     *
     * @param userAuthSocieteDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAuthSocieteDTO save(UserAuthSocieteDTO userAuthSocieteDTO) {
        log.debug("Request to save UserAuthSociete : {}", userAuthSocieteDTO);
        UserAuthSociete userAuthSociete = userAuthSocieteMapper.toEntity(userAuthSocieteDTO);
        userAuthSociete = userAuthSocieteRepository.save(userAuthSociete);
        return userAuthSocieteMapper.toDto(userAuthSociete);
    }

    /**
     * Update a userAuthSociete.
     *
     * @param userAuthSocieteDTO the entity to save.
     * @return the persisted entity.
     */
    public UserAuthSocieteDTO update(UserAuthSocieteDTO userAuthSocieteDTO) {
        log.debug("Request to update UserAuthSociete : {}", userAuthSocieteDTO);
        UserAuthSociete userAuthSociete = userAuthSocieteMapper.toEntity(userAuthSocieteDTO);
        userAuthSociete = userAuthSocieteRepository.save(userAuthSociete);
        return userAuthSocieteMapper.toDto(userAuthSociete);
    }

    /**
     * Partially update a userAuthSociete.
     *
     * @param userAuthSocieteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserAuthSocieteDTO> partialUpdate(UserAuthSocieteDTO userAuthSocieteDTO) {
        log.debug("Request to partially update UserAuthSociete : {}", userAuthSocieteDTO);

        return userAuthSocieteRepository
            .findById(userAuthSocieteDTO.getId())
            .map(existingUserAuthSociete -> {
                userAuthSocieteMapper.partialUpdate(existingUserAuthSociete, userAuthSocieteDTO);

                return existingUserAuthSociete;
            })
            .map(userAuthSocieteRepository::save)
            .map(userAuthSocieteMapper::toDto);
    }

    /**
     * Get all the userAuthSocietes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserAuthSocieteDTO> findAll() {
        log.debug("Request to get all UserAuthSocietes");
        return userAuthSocieteRepository
            .findAll()
            .stream()
            .map(userAuthSocieteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userAuthSociete by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserAuthSocieteDTO> findOne(Long id) {
        log.debug("Request to get UserAuthSociete : {}", id);
        return userAuthSocieteRepository.findById(id).map(userAuthSocieteMapper::toDto);
    }

    /**
     * Delete the userAuthSociete by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserAuthSociete : {}", id);
        userAuthSocieteRepository.deleteById(id);
    }

    public UserAuthSocieteDTO assignRole(AssignRoleDTO dto) {
        UserAuthSociete auth = userAuthSocieteRepository
            .findBySocieteIdAndContactSocieteId(dto.getSocieteId(), dto.getContactSocieteId())
            .orElse(new UserAuthSociete());

        auth.setSocieteId(dto.getSocieteId());
        auth.setContactSocieteId(dto.getContactSocieteId());
        auth.setRoleContactSocieteId(dto.getRoleContactSocieteId());

        return userAuthSocieteMapper.toDto(userAuthSocieteRepository.save(auth));
    }

    @Transactional(readOnly = true)
    public List<UserAuthSocieteDTO> findBySocieteId(Long societeId) {
        log.debug("Request to get UserAuthSocietes by societeId : {}", societeId);

        return userAuthSocieteRepository.findBySocieteId(societeId).stream().map(userAuthSocieteMapper::toDto).collect(Collectors.toList());
    }
}
