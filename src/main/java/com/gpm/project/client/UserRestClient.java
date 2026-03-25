package com.gpm.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@AuthorizedFeignClient(name = "GPMGATEWAY")
public interface UserRestClient {
    @GetMapping("/api/getCurrentUserId")
    String getCurrentUserId();
}
