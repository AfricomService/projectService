package com.gpm.project.config;

import com.gpm.project.service.RemoteAuthService;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RemoteApiConfig {

    @Bean
    public RestTemplate gatewayRestTemplate(
        RestTemplateBuilder builder,
        BearerTokenInterceptor interceptor,
        @Value("${remote.gateway-url}") String url
    ) {
        return builder
            .rootUri(url)
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(30))
            .additionalInterceptors(interceptor)
            .build();
    }

    @Bean
    public RestTemplate reportRestTemplate(
        RestTemplateBuilder builder,
        BearerTokenInterceptor interceptor,
        @Value("${remote.report-url}") String url
    ) {
        return builder
            .rootUri(url)
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(60))
            .additionalInterceptors(interceptor)
            .build();
    }
}
