package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.TypeRessource} entity.
 */
public class TypeRessourceDTO implements Serializable {

    private Long id;

    private String type;

    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeRessourceDTO)) {
            return false;
        }

        TypeRessourceDTO typeRessourceDTO = (TypeRessourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeRessourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeRessourceDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
