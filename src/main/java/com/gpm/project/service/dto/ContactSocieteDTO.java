package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.ContactSociete} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactSocieteDTO implements Serializable {

    private Long id;

    private String matricule;

    private String nomPrenom;

    private String email;

    private String numTel;

    private Long societeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactSocieteDTO)) {
            return false;
        }

        ContactSocieteDTO contactSocieteDTO = (ContactSocieteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactSocieteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactSocieteDTO{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nomPrenom='" + getNomPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
