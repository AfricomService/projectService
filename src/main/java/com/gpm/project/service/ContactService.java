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

import com.gpm.project.client.UserRestClient;
import com.gpm.project.security.SecurityUtils;
import java.time.ZonedDateTime;

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

    private final UserRestClient userRestClient;

    public ContactService(
        ContactRepository contactRepository,
        ContactMapper contactMapper,
        ClientRepository clientRepository,
        UserRestClient userRestClient
    ) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
        this.clientRepository = clientRepository;
        this.userRestClient = userRestClient;
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

        contact.setStatusCompteKeycloak("DEACTIVE");

        contact.setCreatedAt(ZonedDateTime.now());
        contact.setUpdatedAt(ZonedDateTime.now());
        contact.setCreatedBy(SecurityUtils.getCurrentUserLogin().get());
        contact.setUpdatedBy(SecurityUtils.getCurrentUserLogin().get());
        contact.setUpdatedByUserLogin(userRestClient.getCurrentUserId());
        contact.setCreatedByUserLogin(userRestClient.getCurrentUserId());

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
     * Vérifie si un utilisateur existe déjà dans la table user du gateway
     * (i.e. le contact s'est connecté au moins une fois via Keycloak),
     * en comparant à l'identifiantUnique du contact (en minuscules, car
     * le login est stocké en minuscules côté gateway - cf. UserService.getUser()).
     *
     * @param identifiantUnique l'identifiant unique du contact.
     * @return true si un user existe avec ce login, false sinon.
     */
    public boolean getUserByIdentifiantUnique(String identifiantUnique) {
        if (identifiantUnique == null || identifiantUnique.isBlank()) {
            return false;
        }
        String login = identifiantUnique.toLowerCase();
        log.debug("Vérification de l'existence d'un user gateway pour le login : {}", login);
        try {
            return Boolean.TRUE.equals(userRestClient.existsByLogin(login));
        } catch (Exception e) {
            log.warn("Impossible de vérifier l'existence du user gateway pour le login {} : {}", login, e.getMessage());
            return false;
        }
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

        contact.setUpdatedAt(ZonedDateTime.now());
        contact.setUpdatedBy(SecurityUtils.getCurrentUserLogin().get());
        contact.setUpdatedByUserLogin(userRestClient.getCurrentUserId());

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
     * Search contacts by clientId and nomPrenom (partial, case-insensitive).
     *
     * @param clientId the id of the client.
     * @param nomPrenom the search term for nomPrenom.
     * @return the list of matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> searchContactsByClientId(Long clientId, String nomPrenom) {
        log.debug("Request to search Contacts by clientId : {} and nomPrenom : {}", clientId, nomPrenom);
        return contactRepository
            .findByClientIdAndNomPrenomContainingIgnoreCase(clientId, nomPrenom)
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

    /**
     * Récupère tous les contacts dont le statusCompteKeycloak est "EN_COURS",
     * vérifie pour chacun si l'utilisateur correspondant existe désormais dans
     * la table user du gateway, et passe leur statut à "ACTIF" le cas échéant.
     * L'ensemble du traitement s'exécute dans une seule transaction.
     *
     * @return le nombre de contacts effectivement passés à "ACTIF".
     */
    @Transactional
    public int refreshKeycloakStatusesEnCours() {
        List<Contact> contactsEnCours = contactRepository.findByStatusCompteKeycloak("EN_COURS");

        if (contactsEnCours.isEmpty()) {
            log.debug("Aucun contact avec statusCompteKeycloak=EN_COURS à vérifier");
            return 0;
        }

        log.info("Vérification du statut Keycloak pour {} contact(s) EN_COURS", contactsEnCours.size());

        int updatedCount = 0;
        for (Contact contact : contactsEnCours) {
            try {
                boolean userExists = getUserByIdentifiantUnique(contact.getIdentifiantUnique());
                if (userExists) {
                    contact.setStatusCompteKeycloak("ACTIF");
                    contact.setUpdatedAt(ZonedDateTime.now());
                    contactRepository.save(contact);
                    updatedCount++;
                    log.info("Contact {} : statusCompteKeycloak passé de EN_COURS à ACTIF", contact.getIdentifiantUnique());
                }
            } catch (Exception e) {
                log.warn(
                    "Erreur lors de la vérification du statut Keycloak pour le contact {} : {}",
                    contact.getIdentifiantUnique(),
                    e.getMessage()
                );
            }
        }

        log.info("{} contact(s) passé(s) à ACTIF sur {} vérifié(s)", updatedCount, contactsEnCours.size());
        return updatedCount;
    }
}
