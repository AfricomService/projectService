package com.gpm.project.service;

import com.gpm.project.config.KeycloakAdminProperties;
import com.gpm.project.service.dto.ContactDTO;
import java.security.SecureRandom;
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
     * Récupère un token "service account" (client_credentials) réutilisable pour les
     * appels machine-à-machine (ex: appels Feign lancés depuis un thread @Scheduled,
     * où il n'y a pas de requête HTTP utilisateur dont propager le token).
     *
     * @return le token d'accès, ou null en cas d'échec.
     */
    public String getServiceAccountToken() {
        try {
            return getAdminToken();
        } catch (Exception e) {
            log.warn("Impossible de récupérer un token service account Keycloak : {}", e.getMessage());
            return null;
        }
    }

    /**
     * Crée un utilisateur Keycloak à partir d'un contact.
     * Le username utilisé est l'identifiantUnique du contact.
     */
    public String createUserFromContact(ContactDTO contact) {
        if (contact.getIdentifiantUnique() == null || contact.getIdentifiantUnique().isBlank()) {
            throw new IllegalStateException("Le contact n'a pas encore d'identifiant unique");
        }

        String token = getAdminToken();
        String usersUri = properties.getServerUrl() + "/admin/realms/" + properties.getRealm() + "/users";

        String[] nameParts = contact.getNomPrenom() != null ? contact.getNomPrenom().trim().split("\\s+", 2) : new String[] { "", "" };
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        String defaultPassword = generateRandomPassword();

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
            return defaultPassword;
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalStateException("Un utilisateur Keycloak avec le username '" + contact.getIdentifiantUnique() + "' existe déjà");
        }
    }

    /**
     * Génère un mot de passe aléatoire sécurisé (au moins une majuscule,
     * une minuscule, un chiffre et un caractère spécial).
     */
    private String generateRandomPassword() {
        String upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower = "abcdefghijkmnopqrstuvwxyz";
        String digits = "23456789";
        String special = "!@#$%&*";
        String allChars = upper + lower + digits + special;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        int length = 10;
        for (int i = password.length(); i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        List<Character> chars = new java.util.ArrayList<>();
        for (char c : password.toString().toCharArray()) {
            chars.add(c);
        }
        java.util.Collections.shuffle(chars, random);

        StringBuilder shuffled = new StringBuilder();
        chars.forEach(shuffled::append);

        return shuffled.toString();
    }

    /**
     * Réinitialise le mot de passe d'un utilisateur Keycloak existant.
     * Génère un nouveau mot de passe temporaire que l'utilisateur devra changer
     * à sa prochaine connexion.
     *
     * @param username le username Keycloak (= identifiantUnique du contact).
     * @return le nouveau mot de passe généré.
     */
    public String resetPasswordForUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Le contact n'a pas d'identifiant unique");
        }

        String token = getAdminToken();
        String userId = findUserIdByUsername(username, token);

        if (userId == null) {
            throw new IllegalStateException("Aucun utilisateur Keycloak trouvé pour le username '" + username + "'");
        }

        String resetPasswordUri = properties.getServerUrl() + "/admin/realms/" + properties.getRealm() + "/users/" + userId + "/reset-password";

        String newPassword = generateRandomPassword();

        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", newPassword);
        credential.put("temporary", true); // forcé de changer au prochain login

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credential, headers);

        restTemplate.put(resetPasswordUri, request);
        log.info("Mot de passe réinitialisé pour l'utilisateur Keycloak {}", username);

        return newPassword;
    }

    /**
     * Recherche l'id interne Keycloak (UUID) d'un utilisateur à partir de son username.
     *
     * @param username le username à rechercher.
     * @param token le token admin déjà obtenu.
     * @return l'id Keycloak, ou null si non trouvé.
     */
    private String findUserIdByUsername(String username, String token) {
        String searchUri =
            properties.getServerUrl() + "/admin/realms/" + properties.getRealm() + "/users?username=" + username + "&exact=true";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(searchUri, org.springframework.http.HttpMethod.GET, request, List.class);

        List body = response.getBody();
        if (body == null || body.isEmpty()) {
            return null;
        }

        Map<String, Object> user = (Map<String, Object>) body.get(0);
        return (String) user.get("id");
    }
}
