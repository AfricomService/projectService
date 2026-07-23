package com.gpm.project.service;

import com.gpm.project.domain.ContactSociete;
import com.gpm.project.repository.ContactSocieteRepository;
import com.gpm.project.service.dto.ContactSocieteDTO;
import com.gpm.project.service.mapper.ContactSocieteMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ContactSociete}.
 */
@Service
@Transactional
public class ContactSocieteService {

    private final Logger log = LoggerFactory.getLogger(ContactSocieteService.class);

    private final ContactSocieteRepository contactSocieteRepository;

    private final ContactSocieteMapper contactSocieteMapper;

    public ContactSocieteService(ContactSocieteRepository contactSocieteRepository, ContactSocieteMapper contactSocieteMapper) {
        this.contactSocieteRepository = contactSocieteRepository;
        this.contactSocieteMapper = contactSocieteMapper;
    }

    public List<ContactSocieteDTO> findAllBySocieteId(Long societeId) {
        log.debug("Request to get all ContactSocietes by SocieteId: {}", societeId);
        List<ContactSociete> contactSocietes = contactSocieteRepository.findAllBySocieteId(societeId);
        return contactSocieteMapper.toDto(contactSocietes);
    }

    public ContactSocieteDTO save(ContactSocieteDTO contactSocieteDTO) {
        log.debug("Request to save ContactSociete : {}", contactSocieteDTO);
        ContactSociete contactSociete = contactSocieteMapper.toEntity(contactSocieteDTO);
        contactSociete = contactSocieteRepository.save(contactSociete);
        return contactSocieteMapper.toDto(contactSociete);
    }

    /**
     * Update a contactSociete.
     *
     * @param contactSocieteDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactSocieteDTO update(ContactSocieteDTO contactSocieteDTO) {
        log.debug("Request to update ContactSociete : {}", contactSocieteDTO);
        ContactSociete contactSociete = contactSocieteMapper.toEntity(contactSocieteDTO);
        contactSociete = contactSocieteRepository.save(contactSociete);
        return contactSocieteMapper.toDto(contactSociete);
    }

    /**
     * Partially update a contactSociete.
     *
     * @param contactSocieteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactSocieteDTO> partialUpdate(ContactSocieteDTO contactSocieteDTO) {
        log.debug("Request to partially update ContactSociete : {}", contactSocieteDTO);

        return contactSocieteRepository
            .findById(contactSocieteDTO.getId())
            .map(existingContactSociete -> {
                contactSocieteMapper.partialUpdate(existingContactSociete, contactSocieteDTO);

                return existingContactSociete;
            })
            .map(contactSocieteRepository::save)
            .map(contactSocieteMapper::toDto);
    }

    /**
     * Get all the contactSocietes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactSocieteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactSocietes");
        return contactSocieteRepository.findAll(pageable).map(contactSocieteMapper::toDto);
    }

    /**
     * Get one contactSociete by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactSocieteDTO> findOne(Long id) {
        log.debug("Request to get ContactSociete : {}", id);
        return contactSocieteRepository.findById(id).map(contactSocieteMapper::toDto);
    }

    /**
     * Delete the contactSociete by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ContactSociete : {}", id);
        contactSocieteRepository.deleteById(id);
    }
}
