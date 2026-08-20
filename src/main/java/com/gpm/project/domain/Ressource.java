package com.gpm.project.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

/**
 * A Ressource.
 */
@Entity
@Table(name = "ressource")
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ressource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "code")
    private String code;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "description")
    private String description;

    @Column(name = "date_mise_en_service")
    private ZonedDateTime dateMiseEnService;

    @Column(name = "date_derniere_maintenance")
    private ZonedDateTime dateDerniereMaintenance;

    @Column(name = "date_prochaine_maintenance")
    private ZonedDateTime dateProchaineMaintenance;

    @Column(name = "type_ressource_id")
    private Long typeRessourceId;

    @Column(name = "statut")
    private String statut;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "additional_info")
    private List<Map<String, String>> additionalInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ressource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Ressource nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return this.code;
    }

    public Ressource code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public Ressource categorie(String categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return this.description;
    }

    public Ressource description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDateMiseEnService() {
        return this.dateMiseEnService;
    }

    public Ressource dateMiseEnService(ZonedDateTime dateMiseEnService) {
        this.setDateMiseEnService(dateMiseEnService);
        return this;
    }

    public void setDateMiseEnService(ZonedDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public ZonedDateTime getDateDerniereMaintenance() {
        return this.dateDerniereMaintenance;
    }

    public Ressource dateDerniereMaintenance(ZonedDateTime dateDerniereMaintenance) {
        this.setDateDerniereMaintenance(dateDerniereMaintenance);
        return this;
    }

    public void setDateDerniereMaintenance(ZonedDateTime dateDerniereMaintenance) {
        this.dateDerniereMaintenance = dateDerniereMaintenance;
    }

    public ZonedDateTime getDateProchaineMaintenance() {
        return this.dateProchaineMaintenance;
    }

    public Ressource dateProchaineMaintenance(ZonedDateTime dateProchaineMaintenance) {
        this.setDateProchaineMaintenance(dateProchaineMaintenance);
        return this;
    }

    public void setDateProchaineMaintenance(ZonedDateTime dateProchaineMaintenance) {
        this.dateProchaineMaintenance = dateProchaineMaintenance;
    }

    public Long getTypeRessourceId() {
        return this.typeRessourceId;
    }

    public Ressource typeRessourceId(Long typeRessourceId) {
        this.setTypeRessourceId(typeRessourceId);
        return this;
    }

    public void setTypeRessourceId(Long typeRessourceId) {
        this.typeRessourceId = typeRessourceId;
    }

    public String getStatut() {
        return this.statut;
    }

    public Ressource statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<Map<String, String>> getAdditionalInfo() {
        return this.additionalInfo;
    }

    public Ressource additionalInfo(List<Map<String, String>> additionalInfo) {
        this.setAdditionalInfo(additionalInfo);
        return this;
    }

    public void setAdditionalInfo(List<Map<String, String>> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ressource)) {
            return false;
        }
        return id != null && id.equals(((Ressource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ressource{" +
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
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            "}";
    }
}
