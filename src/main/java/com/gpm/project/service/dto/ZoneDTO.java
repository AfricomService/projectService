package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.Zone} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ZoneDTO implements Serializable {

    private Long id;

    @NotNull
    private String nom;

    private VilleDTO ville;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public VilleDTO getVille() {
        return ville;
    }

    public void setVille(VilleDTO ville) {
        this.ville = ville;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZoneDTO)) {
            return false;
        }

        ZoneDTO zoneDTO = (ZoneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, zoneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZoneDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", ville=" + getVille() +
            "}";
    }
}
