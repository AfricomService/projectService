package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Societe.
 */
@Entity
@Table(name = "societe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Societe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @Column(name = "raison_sociale_abrege")
    private String raisonSocialeAbrege;

    @NotNull
    @Column(name = "identifiant_unique", nullable = false, unique = true)
    private String identifiantUnique;

    @Column(name = "registre_commerce")
    private String registreCommerce;

    @Column(name = "code_article")
    private String codeArticle;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "code_postal")
    private Integer codePostal;

    @NotNull
    @Column(name = "pays", nullable = false)
    private String pays;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "email")
    private String email;

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

    @OneToMany(mappedBy = "societe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vehicules", "societe" }, allowSetters = true)
    private Set<Agence> agences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Societe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return this.raisonSociale;
    }

    public Societe raisonSociale(String raisonSociale) {
        this.setRaisonSociale(raisonSociale);
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getRaisonSocialeAbrege() {
        return this.raisonSocialeAbrege;
    }

    public Societe raisonSocialeAbrege(String raisonSocialeAbrege) {
        this.setRaisonSocialeAbrege(raisonSocialeAbrege);
        return this;
    }

    public void setRaisonSocialeAbrege(String raisonSocialeAbrege) {
        this.raisonSocialeAbrege = raisonSocialeAbrege;
    }

    public String getIdentifiantUnique() {
        return this.identifiantUnique;
    }

    public Societe identifiantUnique(String identifiantUnique) {
        this.setIdentifiantUnique(identifiantUnique);
        return this;
    }

    public void setIdentifiantUnique(String identifiantUnique) {
        this.identifiantUnique = identifiantUnique;
    }

    public String getRegistreCommerce() {
        return this.registreCommerce;
    }

    public Societe registreCommerce(String registreCommerce) {
        this.setRegistreCommerce(registreCommerce);
        return this;
    }

    public void setRegistreCommerce(String registreCommerce) {
        this.registreCommerce = registreCommerce;
    }

    public String getCodeArticle() {
        return this.codeArticle;
    }

    public Societe codeArticle(String codeArticle) {
        this.setCodeArticle(codeArticle);
        return this;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Societe adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getCodePostal() {
        return this.codePostal;
    }

    public Societe codePostal(Integer codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return this.pays;
    }

    public Societe pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Societe telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return this.fax;
    }

    public Societe fax(String fax) {
        this.setFax(fax);
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return this.email;
    }

    public Societe email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Societe createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Societe updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Societe createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return this.createdByUserLogin;
    }

    public Societe createdByUserLogin(String createdByUserLogin) {
        this.setCreatedByUserLogin(createdByUserLogin);
        return this;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Societe updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return this.updatedByUserLogin;
    }

    public Societe updatedByUserLogin(String updatedByUserLogin) {
        this.setUpdatedByUserLogin(updatedByUserLogin);
        return this;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public Set<Agence> getAgences() {
        return this.agences;
    }

    public void setAgences(Set<Agence> agences) {
        if (this.agences != null) {
            this.agences.forEach(i -> i.setSociete(null));
        }
        if (agences != null) {
            agences.forEach(i -> i.setSociete(this));
        }
        this.agences = agences;
    }

    public Societe agences(Set<Agence> agences) {
        this.setAgences(agences);
        return this;
    }

    public Societe addAgences(Agence agence) {
        this.agences.add(agence);
        agence.setSociete(this);
        return this;
    }

    public Societe removeAgences(Agence agence) {
        this.agences.remove(agence);
        agence.setSociete(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Societe)) {
            return false;
        }
        return id != null && id.equals(((Societe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Societe{" +
            "id=" + getId() +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", raisonSocialeAbrege='" + getRaisonSocialeAbrege() + "'" +
            ", identifiantUnique='" + getIdentifiantUnique() + "'" +
            ", registreCommerce='" + getRegistreCommerce() + "'" +
            ", codeArticle='" + getCodeArticle() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", codePostal=" + getCodePostal() +
            ", pays='" + getPays() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", fax='" + getFax() + "'" +
            ", email='" + getEmail() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdByUserLogin='" + getCreatedByUserLogin() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedByUserLogin='" + getUpdatedByUserLogin() + "'" +
            "}";
    }
}
