package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.DetailRessource} entity.
 */
public class DetailRessourceDTO implements Serializable {

    private Long id;

    private Boolean status;

    private String label;

    private String code;

    private Boolean required;

    private String inputType;

    private List<Map<String, String>> multipleChoiceOption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public List<Map<String, String>> getMultipleChoiceOption() {
        return multipleChoiceOption;
    }

    public void setMultipleChoiceOption(List<Map<String, String>> multipleChoiceOption) {
        this.multipleChoiceOption = multipleChoiceOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailRessourceDTO)) {
            return false;
        }

        DetailRessourceDTO detailRessourceDTO = (DetailRessourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, detailRessourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailRessourceDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", label='" + getLabel() + "'" +
            ", code='" + getCode() + "'" +
            ", required='" + getRequired() + "'" +
            ", inputType='" + getInputType() + "'" +
            ", multipleChoiceOption='" + getMultipleChoiceOption() + "'" +
            "}";
    }
}
