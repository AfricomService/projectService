package com.gpm.project.service.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

public class AffaireRapportTemplatesUpdateDTO {

    @NotNull
    private List<Long> rapportTemplateIds;

    public List<Long> getRapportTemplateIds() {
        return rapportTemplateIds;
    }

    public void setRapportTemplateIds(List<Long> rapportTemplateIds) {
        this.rapportTemplateIds = rapportTemplateIds;
    }
}
