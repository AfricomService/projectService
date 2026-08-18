package com.gpm.project.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.Ressource} entity.
 */
public class RessourceDTO implements Serializable {

    private Long id;

    private String nom;

    private String code;

    private String categorie;

    private String description;

    private ZonedDateTime dateMiseEnService;

    private ZonedDateTime dateDerniereMaintenance;

    private ZonedDateTime dateProchaineMaintenance;

    private Long typeRessourceId;

    private String statut;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(ZonedDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public ZonedDateTime getDateDerniereMaintenance() {
        return dateDerniereMaintenance;
    }

    public void setDateDerniereMaintenance(ZonedDateTime dateDerniereMaintenance) {
        this.dateDerniereMaintenance = dateDerniereMaintenance;
    }

    public ZonedDateTime getDateProchaineMaintenance() {
        return dateProchaineMaintenance;
    }

    public void setDateProchaineMaintenance(ZonedDateTime dateProchaineMaintenance) {
        this.dateProchaineMaintenance = dateProchaineMaintenance;
    }

    public Long getTypeRessourceId() {
        return typeRessourceId;
    }

    public void setTypeRessourceId(Long typeRessourceId) {
        this.typeRessourceId = typeRessourceId;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RessourceDTO)) {
            return false;
        }

        RessourceDTO ressourceDTO = (RessourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ressourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RessourceDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", code='" + getCode() + "'" +
            ", categorie='" + getCategorie() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateMiseEnService='" + getDateMiseEnService() + "'" +
            ", dateDerniereMaintenance='" + getDateDerniereMaintenance() + "'" +
            ", dateProchaineMaintenance='" + getDateProchaineMaintenance() + "'" +
            ", typeRessourceId=" + getTypeRessourceId() +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
