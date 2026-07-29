package com.gpm.project.client;

import com.gpm.project.security.oauth2.AuthorizationHeaderUtil;
import com.gpm.project.service.KeycloakAdminService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.Optional;

public class TokenRelayRequestInterceptor implements RequestInterceptor {

    public static final String AUTHORIZATION = "Authorization";

    private final AuthorizationHeaderUtil authorizationHeaderUtil;

    private final KeycloakAdminService keycloakAdminService;

    public TokenRelayRequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil, KeycloakAdminService keycloakAdminService) {
        super();
        this.authorizationHeaderUtil = authorizationHeaderUtil;
        this.keycloakAdminService = keycloakAdminService;
    }

    @Override
    public void apply(RequestTemplate template) {
        Optional<String> authorizationHeader = authorizationHeaderUtil.getAuthorizationHeader();

        if (authorizationHeader.isPresent()) {
            template.header(AUTHORIZATION, authorizationHeader.get());
        } else {
            // Pas de requête HTTP utilisateur en cours (ex: appel depuis un thread @Scheduled) :
            // on utilise un token "service account" (client_credentials) en fallback.
            String serviceToken = keycloakAdminService.getServiceAccountToken();
            if (serviceToken != null) {
                template.header(AUTHORIZATION, "Bearer " + serviceToken);
            }
        }
    }
}
