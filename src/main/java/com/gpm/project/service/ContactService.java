package com.gpm.project.service;

import com.gpm.project.domain.Client;
import com.gpm.project.domain.Contact;
import com.gpm.project.repository.ClientRepository;
import com.gpm.project.repository.ContactRepository;
import com.gpm.project.service.dto.ContactDTO;
import com.gpm.project.service.mapper.ContactMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contact}.
 */
@Service
@Transactional
public class ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    private final ClientRepository clientRepository;

    public ContactService(ContactRepository contactRepository, ContactMapper contactMapper, ClientRepository clientRepository) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.clientRepository = clientRepository;
    }

    /**
     * Save a contact.
     *
     * @param contactDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactDTO save(ContactDTO contactDTO) {
        log.debug("Request to save Contact : {}", contactDTO);
        Contact contact = contactMapper.toEntity(contactDTO);

        if (contact.getClient() != null && contact.getClient().getId() != null) {
            String identifiant = genererIdentifiantContact(contact.getClient().getId());
            contact.setIdentifiantUnique(identifiant);
        }

        contact = contactRepository.save(contact);
        return contactMapper.toDto(contact);
    }

    /**
     * Génère l'identifiant unique d'un contact à partir de l'identifiant du client
     * et de son compteur "nextContactNumber", puis incrémente ce compteur.
     * Format attendu : {identifiantClient}-{nextContactNumber sur 3 chiffres} (ex: C-00009-26-001)
     *
     * @param clientId l'id du client parent.
     * @return l'identifiant généré pour le contact.
     */
    public String genererIdentifiantContact(Long clientId) {
        log.debug("Request to generate identifiant for Contact of client : {}", clientId);

        Client client = clientRepository
            .findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client introuvable pour id : " + clientId));

        String identifiantClient = client.getIdentifiantUnique();
        if (identifiantClient == null || identifiantClient.isBlank()) {
            throw new RuntimeException("Le client " + clientId + " n'a pas encore d'identifiant unique");
        }

        Integer numeroActuel = client.getNextContactNumber();
        if (numeroActuel == null) {
            numeroActuel = 1;
        }

        String suffixe = String.format("%03d", numeroActuel);
        String identifiant = identifiantClient + "-" + suffixe;

        client.setNextContactNumber(numeroActuel + 1);
        clientRepository.save(client);

        log.debug("Identifiant Contact généré : {}", identifiant);
        return identifiant;
    }

    /**
     * Update a contact.
     *
     * @param contactDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactDTO update(ContactDTO contactDTO) {
        log.debug("Request to update Contact : {}", contactDTO);
        Contact contact = contactMapper.toEntity(contactDTO);
        contact = contactRepository.save(contact);
        return contactMapper.toDto(contact);
    }

    /**
     * Partially update a contact.
     *
     * @param contactDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactDTO> partialUpdate(ContactDTO contactDTO) {
        log.debug("Request to partially update Contact : {}", contactDTO);

        return contactRepository
            .findById(contactDTO.getId())
            .map(existingContact -> {
                contactMapper.partialUpdate(existingContact, contactDTO);

                return existingContact;
            })
            .map(contactRepository::save)
            .map(contactMapper::toDto);
    }

    /**
     * Get all the contacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        return contactRepository.findAll(pageable).map(contactMapper::toDto);
    }

    /**
     * Get all the contacts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContactDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contactRepository.findAllWithEagerRelationships(pageable).map(contactMapper::toDto);
    }

    /**
     * Get all contacts by clientId.
     *
     * @param clientId the id of the client.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> findContactsByClientId(Long clientId) {
        log.debug("Request to get Contacts by clientId : {}", clientId);
        return contactRepository.findByClientId(clientId).stream().map(contactMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Search contacts by clientId and raisonSociale (partial, case-insensitive).
     *
     * @param clientId the id of the client.
     * @param raisonSociale the search term for raisonSociale.
     * @return the list of matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> searchContactsByClientId(Long clientId, String raisonSociale) {
        log.debug("Request to search Contacts by clientId : {} and raisonSociale : {}", clientId, raisonSociale);
        return contactRepository
            .findByClientIdAndRaisonSocialeContainingIgnoreCase(clientId, raisonSociale)
            .stream()
            .map(contactMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get one contact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactDTO> findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        return contactRepository.findOneWithEagerRelationships(id).map(contactMapper::toDto);
    }

    /**
     * Delete the contact by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.deleteById(id);
    }
}
