package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAuthSociete.
 */
@Entity
@Table(name = "user_auth_societe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAuthSociete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "societe_id")
    private Long societeId;

    @Column(name = "role_contact_societe_id")
    private Long roleContactSocieteId;

    @Column(name = "contact_societe_id")
    private Long contactSocieteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAuthSociete id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSocieteId() {
        return this.societeId;
    }

    public UserAuthSociete societeId(Long societeId) {
        this.setSocieteId(societeId);
        return this;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public Long getRoleContactSocieteId() {
        return this.roleContactSocieteId;
    }

    public UserAuthSociete roleContactSocieteId(Long roleContactSocieteId) {
        this.setRoleContactSocieteId(roleContactSocieteId);
        return this;
    }

    public void setRoleContactSocieteId(Long roleContactSocieteId) {
        this.roleContactSocieteId = roleContactSocieteId;
    }

    public Long getContactSocieteId() {
        return this.contactSocieteId;
    }

    public UserAuthSociete contactSocieteId(Long contactSocieteId) {
        this.setContactSocieteId(contactSocieteId);
        return this;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthSociete)) {
            return false;
        }
        return id != null && id.equals(((UserAuthSociete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAuthSociete{" +
            "id=" + getId() +
            ", societeId=" + getSocieteId() +
            ", roleContactSocieteId=" + getRoleContactSocieteId() +
            ", contactSocieteId=" + getContactSocieteId() +
            "}";
    }
}
