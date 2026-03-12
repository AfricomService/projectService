package com.gpm.project.service.dto;

import com.gpm.project.domain.enumeration.StatutVehicule;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.Vehicule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehiculeDTO implements Serializable {

    private Long id;

    @NotNull
    private String marque;

    @NotNull
    private String type;

    @NotNull
    private String matricule;

    @NotNull
    private Integer nbPlaces;

    private String numCarteGrise;

    private LocalDate dateCirculation;

    @NotNull
    private String typeCarburant;

    private Float chargeFixe;

    private Float tauxConsommation;

    @NotNull
    private StatutVehicule statut;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String createdBy;

    private String createdByUserLogin;

    private String updatedBy;

    private String updatedByUserLogin;

    private AgenceDTO agence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getNumCarteGrise() {
        return numCarteGrise;
    }

    public void setNumCarteGrise(String numCarteGrise) {
        this.numCarteGrise = numCarteGrise;
    }

    public LocalDate getDateCirculation() {
        return dateCirculation;
    }

    public void setDateCirculation(LocalDate dateCirculation) {
        this.dateCirculation = dateCirculation;
    }

    public String getTypeCarburant() {
        return typeCarburant;
    }

    public void setTypeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    public Float getChargeFixe() {
        return chargeFixe;
    }

    public void setChargeFixe(Float chargeFixe) {
        this.chargeFixe = chargeFixe;
    }

    public Float getTauxConsommation() {
        return tauxConsommation;
    }

    public void setTauxConsommation(Float tauxConsommation) {
        this.tauxConsommation = tauxConsommation;
    }

    public StatutVehicule getStatut() {
        return statut;
    }

    public void setStatut(StatutVehicule statut) {
        this.statut = statut;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return createdByUserLogin;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return updatedByUserLogin;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public AgenceDTO getAgence() {
        return agence;
    }

    public void setAgence(AgenceDTO agence) {
        this.agence = agence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehiculeDTO)) {
            return false;
        }

        VehiculeDTO vehiculeDTO = (VehiculeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehiculeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehiculeDTO{" +
            "id=" + getId() +
            ", marque='" + getMarque() + "'" +
            ", type='" + getType() + "'" +
            ", matricule='" + getMatricule() + "'" +
            ", nbPlaces=" + getNbPlaces() +
            ", numCarteGrise='" + getNumCarteGrise() + "'" +
            ", dateCirculation='" + getDateCirculation() + "'" +
            ", typeCarburant='" + getTypeCarburant() + "'" +
            ", chargeFixe=" + getChargeFixe() +
            ", tauxConsommation=" + getTauxConsommation() +
            ", statut='" + getStatut() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdByUserLogin='" + getCreatedByUserLogin() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedByUserLogin='" + getUpdatedByUserLogin() + "'" +
            ", agence=" + getAgence() +
            "}";
    }
}
