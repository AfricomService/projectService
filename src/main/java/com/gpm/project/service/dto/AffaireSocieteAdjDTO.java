package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.AffaireSocieteAdj} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffaireSocieteAdjDTO implements Serializable {

    private Long id;

    private Long affaireId;

    private Long societeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffaireId() {
        return affaireId;
    }

    public void setAffaireId(Long affaireId) {
        this.affaireId = affaireId;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffaireSocieteAdjDTO)) {
            return false;
        }

        AffaireSocieteAdjDTO affaireSocieteAdjDTO = (AffaireSocieteAdjDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affaireSocieteAdjDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffaireSocieteAdjDTO{" +
            "id=" + getId() +
            ", affaireId=" + getAffaireId() +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
