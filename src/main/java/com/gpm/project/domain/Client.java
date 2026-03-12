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
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @NotNull
    @Column(name = "identifiant_unique", nullable = false, unique = true)
    private String identifiantUnique;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

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

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ville", "client" }, allowSetters = true)
    private Set<Site> sites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return this.raisonSociale;
    }

    public Client raisonSociale(String raisonSociale) {
        this.setRaisonSociale(raisonSociale);
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getIdentifiantUnique() {
        return this.identifiantUnique;
    }

    public Client identifiantUnique(String identifiantUnique) {
        this.setIdentifiantUnique(identifiantUnique);
        return this;
    }

    public void setIdentifiantUnique(String identifiantUnique) {
        this.identifiantUnique = identifiantUnique;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Client adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Client telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return this.fax;
    }

    public Client fax(String fax) {
        this.setFax(fax);
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Client createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Client updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Client createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return this.createdByUserLogin;
    }

    public Client createdByUserLogin(String createdByUserLogin) {
        this.setCreatedByUserLogin(createdByUserLogin);
        return this;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Client updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return this.updatedByUserLogin;
    }

    public Client updatedByUserLogin(String updatedByUserLogin) {
        this.setUpdatedByUserLogin(updatedByUserLogin);
        return this;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setClient(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setClient(this));
        }
        this.contacts = contacts;
    }

    public Client contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Client addContacts(Contact contact) {
        this.contacts.add(contact);
        contact.setClient(this);
        return this;
    }

    public Client removeContacts(Contact contact) {
        this.contacts.remove(contact);
        contact.setClient(null);
        return this;
    }

    public Set<Site> getSites() {
        return this.sites;
    }

    public void setSites(Set<Site> sites) {
        if (this.sites != null) {
            this.sites.forEach(i -> i.setClient(null));
        }
        if (sites != null) {
            sites.forEach(i -> i.setClient(this));
        }
        this.sites = sites;
    }

    public Client sites(Set<Site> sites) {
        this.setSites(sites);
        return this;
    }

    public Client addSites(Site site) {
        this.sites.add(site);
        site.setClient(this);
        return this;
    }

    public Client removeSites(Site site) {
        this.sites.remove(site);
        site.setClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", identifiantUnique='" + getIdentifiantUnique() + "'" +
            ", adresse='" + getAdresse() + "'" +
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
