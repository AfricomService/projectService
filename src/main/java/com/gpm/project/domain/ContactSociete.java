package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ContactSociete.
 */
@Entity
@Table(name = "contact_societe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactSociete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "nom_prenom")
    private String nomPrenom;

    @Column(name = "email")
    private String email;

    @Column(name = "num_tel")
    private String numTel;

    @Column(name = "societe_id")
    private Long societeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactSociete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public ContactSociete matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return this.nomPrenom;
    }

    public ContactSociete nomPrenom(String nomPrenom) {
        this.setNomPrenom(nomPrenom);
        return this;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getEmail() {
        return this.email;
    }

    public ContactSociete email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public ContactSociete numTel(String numTel) {
        this.setNumTel(numTel);
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Long getSocieteId() {
        return this.societeId;
    }

    public ContactSociete societeId(Long societeId) {
        this.setSocieteId(societeId);
        return this;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactSociete)) {
            return false;
        }
        return id != null && id.equals(((ContactSociete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactSociete{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nomPrenom='" + getNomPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
