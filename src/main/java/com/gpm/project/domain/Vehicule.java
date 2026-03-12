package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gpm.project.domain.enumeration.StatutVehicule;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicule.
 */
@Entity
@Table(name = "vehicule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "marque", nullable = false)
    private String marque;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "matricule", nullable = false, unique = true)
    private String matricule;

    @NotNull
    @Column(name = "nb_places", nullable = false)
    private Integer nbPlaces;

    @Column(name = "num_carte_grise")
    private String numCarteGrise;

    @Column(name = "date_circulation")
    private LocalDate dateCirculation;

    @NotNull
    @Column(name = "type_carburant", nullable = false)
    private String typeCarburant;

    @Column(name = "charge_fixe")
    private Float chargeFixe;

    @Column(name = "taux_consommation")
    private Float tauxConsommation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutVehicule statut;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_by_user_login")
    private String createdByUserLogin;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_by_user_login")
    private String updatedByUserLogin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "vehicules", "societe" }, allowSetters = true)
    private Agence agence;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return this.marque;
    }

    public Vehicule marque(String marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getType() {
        return this.type;
    }

    public Vehicule type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Vehicule matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Integer getNbPlaces() {
        return this.nbPlaces;
    }

    public Vehicule nbPlaces(Integer nbPlaces) {
        this.setNbPlaces(nbPlaces);
        return this;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public String getNumCarteGrise() {
        return this.numCarteGrise;
    }

    public Vehicule numCarteGrise(String numCarteGrise) {
        this.setNumCarteGrise(numCarteGrise);
        return this;
    }

    public void setNumCarteGrise(String numCarteGrise) {
        this.numCarteGrise = numCarteGrise;
    }

    public LocalDate getDateCirculation() {
        return this.dateCirculation;
    }

    public Vehicule dateCirculation(LocalDate dateCirculation) {
        this.setDateCirculation(dateCirculation);
        return this;
    }

    public void setDateCirculation(LocalDate dateCirculation) {
        this.dateCirculation = dateCirculation;
    }

    public String getTypeCarburant() {
        return this.typeCarburant;
    }

    public Vehicule typeCarburant(String typeCarburant) {
        this.setTypeCarburant(typeCarburant);
        return this;
    }

    public void setTypeCarburant(String typeCarburant) {
        this.typeCarburant = typeCarburant;
    }

    public Float getChargeFixe() {
        return this.chargeFixe;
    }

    public Vehicule chargeFixe(Float chargeFixe) {
        this.setChargeFixe(chargeFixe);
        return this;
    }

    public void setChargeFixe(Float chargeFixe) {
        this.chargeFixe = chargeFixe;
    }

    public Float getTauxConsommation() {
        return this.tauxConsommation;
    }

    public Vehicule tauxConsommation(Float tauxConsommation) {
        this.setTauxConsommation(tauxConsommation);
        return this;
    }

    public void setTauxConsommation(Float tauxConsommation) {
        this.tauxConsommation = tauxConsommation;
    }

    public StatutVehicule getStatut() {
        return this.statut;
    }

    public Vehicule statut(StatutVehicule statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutVehicule statut) {
        this.statut = statut;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Vehicule createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Vehicule updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Vehicule createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return this.createdByUserLogin;
    }

    public Vehicule createdByUserLogin(String createdByUserLogin) {
        this.setCreatedByUserLogin(createdByUserLogin);
        return this;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Vehicule updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return this.updatedByUserLogin;
    }

    public Vehicule updatedByUserLogin(String updatedByUserLogin) {
        this.setUpdatedByUserLogin(updatedByUserLogin);
        return this;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public Agence getAgence() {
        return this.agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public Vehicule agence(Agence agence) {
        this.setAgence(agence);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicule)) {
            return false;
        }
        return id != null && id.equals(((Vehicule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicule{" +
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
            "}";
    }
}
