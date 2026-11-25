package com.gpm.project.service;

import com.gpm.project.domain.RapportTemplateAffaire;
import com.gpm.project.repository.RapportTemplateAffaireRepository;
import com.gpm.project.service.dto.RemoteRapportTemplateDTO;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional
public class RapportTemplateAffaireService {

    private static final String OBJECT_TYPE = "AFFAIRE";

    private final Logger log = LoggerFactory.getLogger(RapportTemplateAffaireService.class);

    private final RapportTemplateAffaireRepository rapportTemplateAffaireRepository;

    private final AclUtilService aclUtilService;

    private final ReportApiClient reportApiClient;

    public RapportTemplateAffaireService(
        RapportTemplateAffaireRepository rapportTemplateAffaireRepository,
        AclUtilService aclUtilService,
        ReportApiClient reportApiClient
    ) {
        this.rapportTemplateAffaireRepository = rapportTemplateAffaireRepository;
        this.aclUtilService = aclUtilService;
        this.reportApiClient = reportApiClient;
    }

    /**
     * Remplace la sélection de rapport templates associés à une affaire.
     * La liste fournie représente l'état final souhaité (full replace, comme updateSocieteAssociees).
     */
    public void replaceAssignments(Long affaireId, List<Long> rapportTemplateIds) {
        assertWrite(affaireId);

        // déduplication + null-safe, l'ordre de saisie est préservé
        Set<Long> uniqueIds = rapportTemplateIds == null
            ? new LinkedHashSet<>()
            : rapportTemplateIds.stream().filter(java.util.Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));

        rapportTemplateAffaireRepository.deleteByAffaireId(affaireId);

        for (Long templateId : uniqueIds) {
            RapportTemplateAffaire liaison = new RapportTemplateAffaire();
            liaison.setAffaireId(affaireId);
            liaison.setRapportTemplateId(templateId);
            rapportTemplateAffaireRepository.save(liaison);
        }

        log.debug("Affaire {} : {} rapport templates associés", affaireId, uniqueIds.size());
    }

    /**
     * IDs des rapport templates associés à l'affaire (dans l'ordre d'insertion).
     */
    @Transactional(readOnly = true)
    public List<Long> getAssignedTemplateIds(Long affaireId) {
        assertRead(affaireId);
        return rapportTemplateAffaireRepository
            .findByAffaireId(affaireId)
            .stream()
            .map(RapportTemplateAffaire::getRapportTemplateId)
            .collect(Collectors.toList());
    }

    /**
     * Détail (via report-api) des templates associés.
     * Les templates supprimés côté report-api sont ignorés silencieusement.
     */
    @Transactional(readOnly = true)
    public List<RemoteRapportTemplateDTO> getAssignedTemplates(Long affaireId) {
        List<Long> ids = getAssignedTemplateIds(affaireId);
        List<RemoteRapportTemplateDTO> result = new ArrayList<>();

        for (Long id : ids) {
            try {
                result.add(reportApiClient.getTemplate(id));
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    log.warn("Rapport template {} associé à l'affaire {} introuvable côté report-api, ignoré", id, affaireId);
                } else {
                    throw e;
                }
            }
        }
        return result;
    }

    private void assertWrite(Long affaireId) {
        if (!aclUtilService.canWrite(OBJECT_TYPE, affaireId)) {
            throw new AccessDeniedException("Pas d'accès en écriture à l'affaire " + affaireId);
        }
    }

    private void assertRead(Long affaireId) {
        if (!aclUtilService.canRead(OBJECT_TYPE, affaireId)) {
            throw new AccessDeniedException("Pas d'accès en lecture à l'affaire " + affaireId);
        }
    }
}
