package com.gpm.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AuthorizedFeignClient(name = "GPMGATEWAY")
public interface UserRestClient {
    @GetMapping("/api/getCurrentUserId")
    String getCurrentUserId();

    @GetMapping("/api/users/exists")
    Boolean existsByLogin(@RequestParam("login") String login);
}
