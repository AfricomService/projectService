package com.gpm.project.service;

import com.gpm.project.config.KeycloakAdminProperties;
import com.gpm.project.service.dto.ContactDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakAdminService {

    private final Logger log = LoggerFactory.getLogger(KeycloakAdminService.class);

    private final KeycloakAdminProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();

    public KeycloakAdminService(KeycloakAdminProperties properties) {
        this.properties = properties;
    }

    private String getAdminToken() {
        String tokenUri = properties.getServerUrl() + "/realms/" + properties.getRealm() + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", properties.getClientId());
        body.add("client_secret", properties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    /**
     * Crée un utilisateur Keycloak à partir d'un contact.
     * Le username utilisé est l'identifiantUnique du contact.
     */
    public void createUserFromContact(ContactDTO contact) {
        if (contact.getIdentifiantUnique() == null || contact.getIdentifiantUnique().isBlank()) {
            throw new IllegalStateException("Le contact n'a pas encore d'identifiant unique");
        }

        String token = getAdminToken();
        String usersUri = properties.getServerUrl() + "/admin/realms/" + properties.getRealm() + "/users";

        String[] nameParts = contact.getNomPrenom() != null ? contact.getNomPrenom().trim().split("\\s+", 2) : new String[] { "", "" };
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        String defaultPassword = "123456";

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", defaultPassword);
        credentials.put("temporary", true); // forcé de changer au premier login

        Map<String, Object> body = new HashMap<>();
        body.put("username", contact.getIdentifiantUnique());
        body.put("firstName", firstName);
        body.put("lastName", lastName);
        body.put("email", contact.getEmail());
        body.put("emailVerified", true);
        body.put("enabled", true);
        body.put("credentials", List.of(credentials));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(usersUri, request, Void.class);
            log.info("Utilisateur Keycloak créé pour le contact {}", contact.getIdentifiantUnique());
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalStateException("Un utilisateur Keycloak avec le username '" + contact.getIdentifiantUnique() + "' existe déjà");
        }
    }
}
