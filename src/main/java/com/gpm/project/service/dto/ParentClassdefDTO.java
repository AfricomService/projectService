package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.ParentClassdef} entity.
 */
public class ParentClassdefDTO implements Serializable {

    private Long id;

    private String type;

    private String formule;

    private Long sequenceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParentClassdefDTO)) {
            return false;
        }

        ParentClassdefDTO parentClassdefDTO = (ParentClassdefDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parentClassdefDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParentClassdefDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", formule='" + getFormule() + "'" +
            ", sequenceId=" + getSequenceId() +
            "}";
    }
}
