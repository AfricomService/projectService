package com.gpm.project.service;

import com.gpm.project.client.UserRestClient;
import com.gpm.project.domain.*;
import com.gpm.project.domain.enumeration.AclPermission;
import com.gpm.project.domain.enumeration.StatutAffaire;
import com.gpm.project.repository.*;
import com.gpm.project.security.AuthoritiesConstants; // << ACL
import com.gpm.project.security.SecurityUtils;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.mapper.AffaireMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException; // << ACL
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Affaire}.
 */
@Service
@Transactional
public class AffaireService {

    private static final String OBJECT_TYPE = "AFFAIRE"; // matches acl_entry.object_type

    private final Logger log = LoggerFactory.getLogger(AffaireService.class);

    private final AffaireRepository affaireRepository;

    private final AffaireMapper affaireMapper;

    private final UserRestClient userRestClient;

    private final AffaireSocieteAdjRepository affaireSocieteAdjRepository;

    private final NumsequentielleService numsequentielleService;

    private final AclUtilService aclUtilService;

    private final RoleContactSocieteRepository roleContactSocieteRepository;

    private final UserAuthSocieteRepository userAuthSocieteRepository;

    private final ContactSocieteRepository contactSocieteRepository;

    public AffaireService(
        AffaireRepository affaireRepository,
        AffaireMapper affaireMapper,
        UserRestClient userRestClient,
        AffaireSocieteAdjRepository affaireSocieteAdjRepository,
        NumsequentielleService numsequentielleService,
        AclUtilService aclUtilService,
        RoleContactSocieteRepository roleContactSocieteRepository,
        UserAuthSocieteRepository userAuthSocieteRepository,
        ContactSocieteRepository contactSocieteRepository
    ) {
        this.affaireRepository = affaireRepository;
        this.affaireMapper = affaireMapper;
        this.userRestClient = userRestClient;
        this.affaireSocieteAdjRepository = affaireSocieteAdjRepository;
        this.numsequentielleService = numsequentielleService;
        this.aclUtilService = aclUtilService;
        this.roleContactSocieteRepository = roleContactSocieteRepository;
        this.userAuthSocieteRepository = userAuthSocieteRepository;
        this.contactSocieteRepository = contactSocieteRepository;
    }

    public void changeStatut(String newStatut, Long affaireId) {
        assertCanChangeStatut(affaireId); // replaces assertWrite

        Affaire affaire = affaireRepository.findById(affaireId).orElseThrow(() -> new RuntimeException("Affaire not found"));

        try {
            StatutAffaire statut = StatutAffaire.valueOf(newStatut);
            affaire.setStatut(statut);

            switch (statut) {
                case EtudeOpportunite:
                    aclUtilService.grantToUser(
                        OBJECT_TYPE,
                        affaireId,
                        affaire.getClient().getIdentifiantUnique().toLowerCase(),
                        AclPermission.READ
                    );
                    break;
                case ExecutionDesTravaux:
                    // Works for a first activation AND for a reactivation after Fin:
                    // 1. everybody drops to READ only (creator, logistique, managers, client, old responsable)
                    aclUtilService.makeReadOnlyForAll(OBJECT_TYPE, affaireId);

                    // 2. only the current project manager gets WRITE
                    String responsable = affaire.getResponsableProjetUserLogin();
                    if (responsable != null && !responsable.isBlank()) {
                        aclUtilService.grantToUser(OBJECT_TYPE, affaireId, responsable.toLowerCase(), AclPermission.WRITE);
                    }

                    // 3. client keeps READ
                    aclUtilService.grantToUser(
                        OBJECT_TYPE,
                        affaireId,
                        affaire.getClient().getIdentifiantUnique().toLowerCase(),
                        AclPermission.READ
                    );
                    break;
                case ClotureProjet:
                    break;
                case Fin:
                    aclUtilService.makeReadOnlyForAll(OBJECT_TYPE, affaireId);
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Statut invalide : " + newStatut);
        }

        affaire.setUpdatedAt(ZonedDateTime.now());
        affaire.setUpdatedBy(SecurityUtils.getCurrentUserLogin().orElse("SYSTEM"));
        affaireRepository.save(affaire);
    }

    // << ACL : who may change the statut
    private void assertCanChangeStatut(Long id) {
        boolean privileged = SecurityUtils.hasCurrentUserAnyOfAuthorities(
            AuthoritiesConstants.ADMIN,
            AuthoritiesConstants.ACTIVATE_AFFAIRE
        );
        if (!privileged && !aclUtilService.canWrite(OBJECT_TYPE, id)) {
            throw new AccessDeniedException("Pas d'accès en écriture à l'affaire " + id);
        }
    }

    /**
     * Save a affaire.
     *
     * @param affaireDTO the entity to save.
     * @return the persisted entity.
     */

    public AffaireDTO save(AffaireDTO affaireDTO) {
        log.debug("Request to save Affaire : {}", affaireDTO);

        ZonedDateTime now = ZonedDateTime.now();
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        affaireDTO.setCreatedAt(now);
        affaireDTO.setUpdatedAt(now);
        affaireDTO.setCreatedBy(currentLogin);
        affaireDTO.setUpdatedBy(currentLogin);
        affaireDTO.setCreatedByUserLogin(userRestClient.getCurrentUserId());
        affaireDTO.setUpdatedByUserLogin(userRestClient.getCurrentUserId());
        affaireDTO.setIdentifiantUnique(numsequentielleService.genererIdentifiantAffaire("AFFAIRE"));

        Affaire affaire = affaireRepository.save(affaireMapper.toEntity(affaireDTO));
        Long affaireId = affaire.getId();
        Long societeId = affaire.getSocieteId();

        // 1. Creator: READ + WRITE
        aclUtilService.grantOwner(OBJECT_TYPE, affaireId, currentLogin.toLowerCase());

        // 2. Company logistics contacts: WRITE
        grantRolePermission("LOGISTIQUE", societeId, affaireId, AclPermission.WRITE);

        // 3. Project manager: READ
        aclUtilService.grantToUser(OBJECT_TYPE, affaireId, affaireDTO.getResponsableProjetUserLogin().toLowerCase(), AclPermission.READ);

        // 4. Company managers: READ
        grantRolePermission("MANAGER", societeId, affaireId, AclPermission.READ);

        return withPermissions(affaireMapper.toDto(affaire));
    }

    private void grantRolePermission(String roleCode, Long societeId, Long affaireId, AclPermission permission) {
        RoleContactSociete role = roleContactSocieteRepository
            .findByCode(roleCode)
            .orElseThrow(() -> new RuntimeException("RoleContactSociete " + roleCode + " not found"));

        List<UserAuthSociete> userAuthSocietes = userAuthSocieteRepository.findAllByRoleContactSocieteIdAndSocieteId(
            role.getId(),
            societeId
        );

        for (UserAuthSociete userAuthSociete : userAuthSocietes) {
            contactSocieteRepository
                .findById(userAuthSociete.getContactSocieteId())
                .map(ContactSociete::getMatricule)
                .filter(matricule -> matricule != null && !matricule.trim().isEmpty())
                .ifPresent(matricule -> aclUtilService.grantToUser(OBJECT_TYPE, affaireId, matricule.toLowerCase(), permission));
        }
    }

    /**
     * Update a affaire.
     *
     * @param affaireDTO the entity to save.
     * @return the persisted entity.
     */
    public AffaireDTO update(AffaireDTO affaireDTO) {
        log.debug("Request to update Affaire : {}", affaireDTO);
        assertWrite(affaireDTO.getId()); // << ACL
        Affaire affaire = affaireMapper.toEntity(affaireDTO);
        affaire = affaireRepository.save(affaire);
        return withPermissions(affaireMapper.toDto(affaire));
    }

    public void updateSocieteAssociees(Long affaireId, List<Long> societeIds) {
        assertWrite(affaireId); // << ACL

        affaireSocieteAdjRepository.deleteByAffaireId(affaireId);

        if (!societeIds.isEmpty()) {
            societeIds.forEach(cl -> {
                AffaireSocieteAdj affaireSocieteAdj = new AffaireSocieteAdj();
                affaireSocieteAdj.setSocieteId(cl.longValue());
                affaireSocieteAdj.setAffaireId(affaireId);
                affaireSocieteAdjRepository.save(affaireSocieteAdj);
            });
        }
    }

    /**
     * Partially update a affaire.
     *
     * @param affaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AffaireDTO> partialUpdate(AffaireDTO affaireDTO) {
        log.debug("Request to partially update Affaire : {}", affaireDTO);
        assertWrite(affaireDTO.getId()); // << ACL

        return affaireRepository
            .findById(affaireDTO.getId())
            .map(existingAffaire -> {
                affaireMapper.partialUpdate(existingAffaire, affaireDTO);

                return existingAffaire;
            })
            .map(affaireRepository::save)
            .map(affaireMapper::toDto)
            .map(this::withPermissions);
    }

    /**
     * Get all the affaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AffaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Affaires");

        // << ACL : bypass admin
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return affaireRepository.findAll(pageable).map(affaireMapper::toDto);
        }

        // << ACL : seulement les affaires accessibles (filtrage en base, pagination préservée)
        List<Long> sidIds = aclUtilService.getCurrentUserSids().stream().map(AclSid::getId).collect(Collectors.toList());
        if (sidIds.isEmpty()) {
            return Page.empty(pageable);
        }
        return affaireRepository.findAllAccessible(sidIds, pageable).map(affaireMapper::toDto);
    }

    /**
     * Get all the affaires with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AffaireDTO> findAllWithEagerRelationships(Pageable pageable) {
        return affaireRepository.findAllWithEagerRelationships(pageable).map(affaireMapper::toDto);
    }

    /**
     * Get all affaires by clientId.
     *
     * @param clientId the id of the client.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AffaireDTO> findAffairesByClientId(Long clientId) {
        log.debug("Request to get Affaires by clientId : {}", clientId);
        return affaireRepository.findByClientId(clientId).stream().map(affaireMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get affaires filtered by statut with a free-text search (designation or numAffaire), paginated.
     *
     * @param statut   the statut to filter on.
     * @param search   the free text search term (can be null/empty).
     * @param pageable the pagination information.
     * @return a page of matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AffaireDTO> findByStatutAndSearch(StatutAffaire statut, String search, Pageable pageable) {
        log.debug("Request to search Affaires by statut : {} and search : {}", statut, search);
        return affaireRepository.findByStatutAndSearch(statut, search, pageable).map(affaireMapper::toDto);
    }

    /**
     * Retourne les ids des affaires dont la désignation, l'identifiant unique
     * ou le numéro contient le terme recherché (plafonné à 200 résultats).
     */
    @Transactional(readOnly = true)
    public List<Long> findIdsBySearch(String search) {
        String term = search == null ? "" : search.trim();
        if (term.isEmpty()) {
            return List.of();
        }
        return affaireRepository.findIdsBySearch(term, PageRequest.of(0, 200));
    }

    /**
     * Get one affaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    private AffaireDTO withPermissions(AffaireDTO dto) {
        dto.setCanRead(aclUtilService.canRead(OBJECT_TYPE, dto.getId()));
        dto.setCanWrite(aclUtilService.canWrite(OBJECT_TYPE, dto.getId()));
        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<AffaireDTO> findOne(Long id) {
        log.debug("Request to get Affaire : {}", id);

        if (!aclUtilService.canRead(OBJECT_TYPE, id)) {
            throw new AccessDeniedException("Pas d'accès en lecture à l'affaire " + id);
        }

        return affaireRepository.findOneWithEagerRelationships(id).map(affaireMapper::toDto).map(this::withPermissions);
    }

    /**
     * Delete the affaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Affaire : {}", id);
        assertWrite(id); // << ACL
        affaireRepository.deleteById(id);
        aclUtilService.deleteAcl(OBJECT_TYPE, id); // << ACL : nettoyage des droits
    }

    /**
     * Search affaires by clientId and designationAffaire (partial, case-insensitive).
     *
     * @param clientId the id of the client.
     * @param designation the search term.
     * @return the list of matching entities.
     */
    @Transactional(readOnly = true)
    public List<AffaireDTO> searchAffairesByClientIdAndDesignation(Long clientId, String designation) {
        log.debug("Request to search Affaires by clientId : {} and designation : {}", clientId, designation);
        return affaireRepository
            .findByClientIdAndDesignationAffaireContainingIgnoreCase(clientId, designation)
            .stream()
            .map(affaireMapper::toDto)
            .collect(Collectors.toList());
    }

    // << ACL : helper privé — lève une 403 si l'utilisateur courant n'a pas le droit WRITE
    private void assertWrite(Long id) {
        if (!aclUtilService.canWrite(OBJECT_TYPE, id)) {
            throw new AccessDeniedException("Pas d'accès en écriture à l'affaire " + id);
        }
    }
}
