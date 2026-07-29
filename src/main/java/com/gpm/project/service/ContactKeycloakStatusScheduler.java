package com.gpm.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Tâche planifiée qui vérifie périodiquement si les contacts dont le compte Keycloak
 * est en statut "EN_COURS" se sont bien connectés au moins une fois (présence dans la
 * table user du gateway). Si oui, le statut passe à "ACTIF".
 */
@Component
public class ContactKeycloakStatusScheduler {

    private final Logger log = LoggerFactory.getLogger(ContactKeycloakStatusScheduler.class);

    private final ContactService contactService;

    public ContactKeycloakStatusScheduler(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Exécuté toutes les 10 minutes. Cron : sec min heure jour mois jour-semaine.
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void refreshKeycloakStatuses() {
        contactService.refreshKeycloakStatusesEnCours();
    }
}
