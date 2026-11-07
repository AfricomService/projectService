package com.gpm.project.service;

import com.gpm.project.service.dto.PageResult;
import com.gpm.project.service.dto.RemoteRapportTemplateDTO;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ReportApiClient {

    private final RestTemplate rt;

    public ReportApiClient(@Qualifier("reportRestTemplate") RestTemplate rt) {
        this.rt = rt;
    }

    /** GET /rapport-templates?page=0&size=20&withAccess=false */
    public PageResult<RemoteRapportTemplateDTO> getTemplates(int page, int size, boolean withAccess) {
        ResponseEntity<List<RemoteRapportTemplateDTO>> res = rt.exchange(
            "/rapport-templates?page={page}&size={size}&withAccess={withAccess}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<RemoteRapportTemplateDTO>>() {},
            page,
            size,
            withAccess
        );

        String totalHeader = res.getHeaders().getFirst("X-Total-Count");
        long total = totalHeader != null ? Long.parseLong(totalHeader) : 0L;
        return new PageResult<>(res.getBody(), total);
    }

    public RemoteRapportTemplateDTO getTemplate(Long id) {
        return rt.getForObject("/rapport-templates/{id}", RemoteRapportTemplateDTO.class, id);
    }

    public List<String> getEmails(Long id) {
        return rt
            .exchange("/rapport-templates/{id}/emails", HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {}, id)
            .getBody();
    }

    /** POST /rapport-templates/addEmail?rapportTemplateId=..&value=.. (request params, not a body) */
    public RemoteRapportTemplateDTO addEmail(Long templateId, String email) {
        URI uri = UriComponentsBuilder
            .fromPath("/rapport-templates/addEmail")
            .queryParam("rapportTemplateId", templateId)
            .queryParam("value", email)
            .build()
            .encode()
            .toUri();
        return rt.postForObject(uri, null, RemoteRapportTemplateDTO.class);
    }

    public RemoteRapportTemplateDTO create(RemoteRapportTemplateDTO dto) {
        return rt.postForObject("/rapport-templates", dto, RemoteRapportTemplateDTO.class);
    }

    public List<RemoteRapportTemplateDTO> findByTechAbility(boolean value) {
        return rt
            .exchange(
                "/rapport-templates/canBeCreatedByTech?canBeCreatedByTech={v}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RemoteRapportTemplateDTO>>() {},
                value
            )
            .getBody();
    }

    public byte[] downloadTemplateFile(Long id) {
        return rt.getForObject("/rapport-templates/{id}/template-file", byte[].class, id);
    }

    /** POST multipart /rapport-templates/uploadImportExcel */

    public void delete(Long id) {
        rt.delete("/rapport-templates/{id}", id);
    }
}
