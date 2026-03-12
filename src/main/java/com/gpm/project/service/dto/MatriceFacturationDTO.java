package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.MatriceFacturation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriceFacturationDTO implements Serializable {

    private Long id;

    @NotNull
    private Float tarifBase;

    @NotNull
    private Float tarifMissionNuit;

    @NotNull
    private Float tarifHebergement;

    @NotNull
    private Float tarifJourFerie;

    @NotNull
    private Float tarifDimanche;

    private AffaireDTO affaire;

    private VilleDTO ville;

    private ZoneDTO zone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTarifBase() {
        return tarifBase;
    }

    public void setTarifBase(Float tarifBase) {
        this.tarifBase = tarifBase;
    }

    public Float getTarifMissionNuit() {
        return tarifMissionNuit;
    }

    public void setTarifMissionNuit(Float tarifMissionNuit) {
        this.tarifMissionNuit = tarifMissionNuit;
    }

    public Float getTarifHebergement() {
        return tarifHebergement;
    }

    public void setTarifHebergement(Float tarifHebergement) {
        this.tarifHebergement = tarifHebergement;
    }

    public Float getTarifJourFerie() {
        return tarifJourFerie;
    }

    public void setTarifJourFerie(Float tarifJourFerie) {
        this.tarifJourFerie = tarifJourFerie;
    }

    public Float getTarifDimanche() {
        return tarifDimanche;
    }

    public void setTarifDimanche(Float tarifDimanche) {
        this.tarifDimanche = tarifDimanche;
    }

    public AffaireDTO getAffaire() {
        return affaire;
    }

    public void setAffaire(AffaireDTO affaire) {
        this.affaire = affaire;
    }

    public VilleDTO getVille() {
        return ville;
    }

    public void setVille(VilleDTO ville) {
        this.ville = ville;
    }

    public ZoneDTO getZone() {
        return zone;
    }

    public void setZone(ZoneDTO zone) {
        this.zone = zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatriceFacturationDTO)) {
            return false;
        }

        MatriceFacturationDTO matriceFacturationDTO = (MatriceFacturationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matriceFacturationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriceFacturationDTO{" +
            "id=" + getId() +
            ", tarifBase=" + getTarifBase() +
            ", tarifMissionNuit=" + getTarifMissionNuit() +
            ", tarifHebergement=" + getTarifHebergement() +
            ", tarifJourFerie=" + getTarifJourFerie() +
            ", tarifDimanche=" + getTarifDimanche() +
            ", affaire=" + getAffaire() +
            ", ville=" + getVille() +
            ", zone=" + getZone() +
            "}";
    }
}
