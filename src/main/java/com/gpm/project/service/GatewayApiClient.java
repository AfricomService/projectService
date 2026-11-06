package com.gpm.project.service;

import com.gpm.project.service.dto.RemoteUserDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GatewayApiClient {

    private final RestTemplate rt;

    public GatewayApiClient(@Qualifier("gatewayRestTemplate") RestTemplate rt) {
        this.rt = rt;
    }

    /** GET /api/users?page=0&size=20 (usually requires ROLE_ADMIN in JHipster) */
    public List<RemoteUserDTO> getUsers(int page, int size) {
        return rt
            .exchange(
                "/users?page={p}&size={s}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RemoteUserDTO>>() {},
                page,
                size
            )
            .getBody();
    }
}
