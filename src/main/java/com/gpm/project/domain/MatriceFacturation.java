package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MatriceFacturation.
 */
@Entity
@Table(name = "matrice_facturation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriceFacturation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tarif_base", nullable = false)
    private Float tarifBase;

    @NotNull
    @Column(name = "tarif_mission_nuit", nullable = false)
    private Float tarifMissionNuit;

    @NotNull
    @Column(name = "tarif_hebergement", nullable = false)
    private Float tarifHebergement;

    @NotNull
    @Column(name = "tarif_jour_ferie", nullable = false)
    private Float tarifJourFerie;

    @NotNull
    @Column(name = "tarif_dimanche", nullable = false)
    private Float tarifDimanche;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Affaire affaire;

    @ManyToOne
    @JsonIgnoreProperties(value = { "zones" }, allowSetters = true)
    private Ville ville;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ville" }, allowSetters = true)
    private Zone zone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MatriceFacturation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTarifBase() {
        return this.tarifBase;
    }

    public MatriceFacturation tarifBase(Float tarifBase) {
        this.setTarifBase(tarifBase);
        return this;
    }

    public void setTarifBase(Float tarifBase) {
        this.tarifBase = tarifBase;
    }

    public Float getTarifMissionNuit() {
        return this.tarifMissionNuit;
    }

    public MatriceFacturation tarifMissionNuit(Float tarifMissionNuit) {
        this.setTarifMissionNuit(tarifMissionNuit);
        return this;
    }

    public void setTarifMissionNuit(Float tarifMissionNuit) {
        this.tarifMissionNuit = tarifMissionNuit;
    }

    public Float getTarifHebergement() {
        return this.tarifHebergement;
    }

    public MatriceFacturation tarifHebergement(Float tarifHebergement) {
        this.setTarifHebergement(tarifHebergement);
        return this;
    }

    public void setTarifHebergement(Float tarifHebergement) {
        this.tarifHebergement = tarifHebergement;
    }

    public Float getTarifJourFerie() {
        return this.tarifJourFerie;
    }

    public MatriceFacturation tarifJourFerie(Float tarifJourFerie) {
        this.setTarifJourFerie(tarifJourFerie);
        return this;
    }

    public void setTarifJourFerie(Float tarifJourFerie) {
        this.tarifJourFerie = tarifJourFerie;
    }

    public Float getTarifDimanche() {
        return this.tarifDimanche;
    }

    public MatriceFacturation tarifDimanche(Float tarifDimanche) {
        this.setTarifDimanche(tarifDimanche);
        return this;
    }

    public void setTarifDimanche(Float tarifDimanche) {
        this.tarifDimanche = tarifDimanche;
    }

    public Affaire getAffaire() {
        return this.affaire;
    }

    public void setAffaire(Affaire affaire) {
        this.affaire = affaire;
    }

    public MatriceFacturation affaire(Affaire affaire) {
        this.setAffaire(affaire);
        return this;
    }

    public Ville getVille() {
        return this.ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public MatriceFacturation ville(Ville ville) {
        this.setVille(ville);
        return this;
    }

    public Zone getZone() {
        return this.zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public MatriceFacturation zone(Zone zone) {
        this.setZone(zone);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatriceFacturation)) {
            return false;
        }
        return id != null && id.equals(((MatriceFacturation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriceFacturation{" +
            "id=" + getId() +
            ", tarifBase=" + getTarifBase() +
            ", tarifMissionNuit=" + getTarifMissionNuit() +
            ", tarifHebergement=" + getTarifHebergement() +
            ", tarifJourFerie=" + getTarifJourFerie() +
            ", tarifDimanche=" + getTarifDimanche() +
            "}";
    }
}
