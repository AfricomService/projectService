package com.gpm.project.config;

import com.gpm.project.service.RemoteAuthService;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class BearerTokenInterceptor implements ClientHttpRequestInterceptor {

    private final RemoteAuthService auth;

    public BearerTokenInterceptor(RemoteAuthService auth) {
        this.auth = auth;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().setBearerAuth(auth.getToken());
        ClientHttpResponse response = execution.execute(request, body);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            response.close();
            auth.invalidate();
            request.getHeaders().setBearerAuth(auth.getToken());
            return execution.execute(request, body);
        }
        return response;
    }
}
