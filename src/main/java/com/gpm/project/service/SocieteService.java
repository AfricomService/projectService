package com.gpm.project.service;

import com.gpm.project.domain.AffaireSocieteAdj;
import com.gpm.project.domain.ContactSociete;
import com.gpm.project.domain.Societe;
import com.gpm.project.repository.AffaireSocieteAdjRepository;
import com.gpm.project.repository.ContactSocieteRepository;
import com.gpm.project.repository.SocieteRepository;
import com.gpm.project.service.dto.ContactSocieteDTO;
import com.gpm.project.service.dto.PersonneDTO;
import com.gpm.project.service.dto.SocieteDTO;
import com.gpm.project.service.mapper.SocieteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Societe}.
 */
@Service
@Transactional
public class SocieteService {

    private final Logger log = LoggerFactory.getLogger(SocieteService.class);

    private final SocieteRepository societeRepository;

    private final SocieteMapper societeMapper;

    private final AffaireSocieteAdjRepository affaireSocieteAdjRepository;

    private final ContactSocieteRepository contactSocieteRepository;

    public SocieteService(
        SocieteRepository societeRepository,
        SocieteMapper societeMapper,
        AffaireSocieteAdjRepository affaireSocieteAdjRepository,
        ContactSocieteRepository contactSocieteRepository
    ) {
        this.societeRepository = societeRepository;
        this.societeMapper = societeMapper;
        this.affaireSocieteAdjRepository = affaireSocieteAdjRepository;
        this.contactSocieteRepository = contactSocieteRepository;
    }

    public List<SocieteDTO> findAllByAffaireId(Long affaireId) {
        List<AffaireSocieteAdj> affaireSocieteAdjs = affaireSocieteAdjRepository.findAllByAffaireId(affaireId);

        List<SocieteDTO> societeDTOS = affaireSocieteAdjs.stream().map(as -> findOne(as.getSocieteId()).get()).toList();

        return societeDTOS;
    }

    /**
     * Save a societe.
     *
     * @param societeDTO the entity to save.
     * @return the persisted entity.
     */
    public SocieteDTO save(SocieteDTO societeDTO) {
        log.debug("Request to save Societe : {}", societeDTO);
        Societe societe = societeMapper.toEntity(societeDTO);
        societe = societeRepository.save(societe);
        return societeMapper.toDto(societe);
    }

    /**
     * Update a societe.
     *
     * @param societeDTO the entity to save.
     * @return the persisted entity.
     */
    public SocieteDTO update(SocieteDTO societeDTO) {
        log.debug("Request to update Societe : {}", societeDTO);
        Societe societe = societeMapper.toEntity(societeDTO);
        societe = societeRepository.save(societe);
        return societeMapper.toDto(societe);
    }

    /**
     * Partially update a societe.
     *
     * @param societeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SocieteDTO> partialUpdate(SocieteDTO societeDTO) {
        log.debug("Request to partially update Societe : {}", societeDTO);

        return societeRepository
            .findById(societeDTO.getId())
            .map(existingSociete -> {
                societeMapper.partialUpdate(existingSociete, societeDTO);

                return existingSociete;
            })
            .map(societeRepository::save)
            .map(societeMapper::toDto);
    }

    /**
     * Get all the societes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocieteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Societes");
        return societeRepository.findAll(pageable).map(societeMapper::toDto);
    }

    /**
     * Get one societe by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocieteDTO> findOne(Long id) {
        log.debug("Request to get Societe : {}", id);
        return societeRepository.findById(id).map(societeMapper::toDto);
    }

    /**
     * Delete the societe by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Societe : {}", id);
        societeRepository.deleteById(id);
    }

    public void assignContactSocieteFromOrgaCare(List<PersonneDTO> personsToAssign, Long societeId) {
        List<ContactSociete> contactSocieteList = new LinkedList<ContactSociete>();

        personsToAssign.forEach(person -> {
            ContactSociete contactSociete = new ContactSociete();
            contactSociete.setSocieteId(societeId);
            contactSociete.setEmail(person.getEmail());
            contactSociete.setMatricule(person.getMatricule());
            contactSociete.setNomPrenom(person.getNomPrenom());

            contactSocieteList.add(contactSociete);
        });

        contactSocieteRepository.saveAll(contactSocieteList);
    }
}
