package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ParentClassdef.
 */
@Entity
@Table(name = "parent_classdef")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ParentClassdef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "formule")
    private String formule;

    @Column(name = "sequence_id")
    private Long sequenceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParentClassdef id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public ParentClassdef type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormule() {
        return this.formule;
    }

    public ParentClassdef formule(String formule) {
        this.setFormule(formule);
        return this;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public Long getSequenceId() {
        return this.sequenceId;
    }

    public ParentClassdef sequenceId(Long sequenceId) {
        this.setSequenceId(sequenceId);
        return this;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParentClassdef)) {
            return false;
        }
        return id != null && id.equals(((ParentClassdef) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParentClassdef{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", formule='" + getFormule() + "'" +
            ", sequenceId=" + getSequenceId() +
            "}";
    }
}
