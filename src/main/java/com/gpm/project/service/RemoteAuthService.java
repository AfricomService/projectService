package com.gpm.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpm.project.service.dto.RemoteUserDTO;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteAuthService {

    private final RestTemplate loginClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String username;
    private final String password;

    private String token;
    private Instant expiresAt = Instant.EPOCH;

    public RemoteAuthService(
        RestTemplateBuilder builder,
        @Value("${remote.gateway-url}") String gatewayUrl,
        @Value("${remote.username}") String username,
        @Value("${remote.password}") String password
    ) {
        this.loginClient =
            builder.rootUri(gatewayUrl).setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(30)).build();
        this.username = username;
        this.password = password;
    }

    public synchronized String getToken() {
        // refresh 60s before real expiry
        if (token == null || Instant.now().isAfter(expiresAt.minusSeconds(60))) {
            login();
        }
        return token;
    }

    public synchronized void invalidate() {
        this.token = null;
    }

    private void login() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("rememberMe", false);

        ResponseEntity<Map> response = loginClient.postForEntity("/authenticate", body, Map.class);
        String idToken = (String) response.getBody().get("id_token");
        if (idToken == null) {
            throw new IllegalStateException("No id_token in authenticate response");
        }
        this.token = idToken;
        this.expiresAt = readExpiry(idToken);
    }

    /** Reads the "exp" claim from the JWT payload (no signature check needed on the client). */
    private Instant readExpiry(String jwt) {
        try {
            String payload = jwt.split("\\.")[1];
            byte[] decoded = Base64.getUrlDecoder().decode(payload);
            JsonNode node = mapper.readTree(decoded);
            return Instant.ofEpochSecond(node.get("exp").asLong());
        } catch (Exception e) {
            return Instant.now().plus(Duration.ofMinutes(30)); // safe fallback
        }
    }
}
