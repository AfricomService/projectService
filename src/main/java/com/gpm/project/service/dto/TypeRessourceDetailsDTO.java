package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.TypeRessourceDetails} entity.
 */
public class TypeRessourceDetailsDTO implements Serializable {

    private Long id;

    private Long typeRessourceId;

    private Long detailRessourceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeRessourceId() {
        return typeRessourceId;
    }

    public void setTypeRessourceId(Long typeRessourceId) {
        this.typeRessourceId = typeRessourceId;
    }

    public Long getDetailRessourceId() {
        return detailRessourceId;
    }

    public void setDetailRessourceId(Long detailRessourceId) {
        this.detailRessourceId = detailRessourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeRessourceDetailsDTO)) {
            return false;
        }

        TypeRessourceDetailsDTO typeRessourceDetailsDTO = (TypeRessourceDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeRessourceDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeRessourceDetailsDTO{" +
            "id=" + getId() +
            ", typeRessourceId=" + getTypeRessourceId() +
            ", detailRessourceId=" + getDetailRessourceId() +
            "}";
    }
}
