package com.gpm.project.web.rest;

import com.gpm.project.service.GatewayApiClient;
import com.gpm.project.service.RemoteAuthService;
import com.gpm.project.service.ReportApiClient;
import com.gpm.project.service.dto.RemoteRapportTemplateDTO;
import com.gpm.project.service.dto.RemoteUserDTO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final ReportApiClient reports;
    private final GatewayApiClient gateway;

    public TestController(ReportApiClient reports, GatewayApiClient gateway) {
        this.reports = reports;
        this.gateway = gateway;
    }

    @GetMapping("/templates")
    public List<RemoteRapportTemplateDTO> templates() {
        return reports.getTemplates(0, 20, false).getContent();
    }

    @GetMapping("/users")
    public List<RemoteUserDTO> users() {
        return gateway.getUsers(0, 20);
    }
}
