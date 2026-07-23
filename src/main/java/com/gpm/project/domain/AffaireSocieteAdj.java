package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AffaireSocieteAdj.
 */
@Entity
@Table(name = "affaire_societe_adj")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffaireSocieteAdj implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "affaire_id")
    private Long affaireId;

    @Column(name = "societe_id")
    private Long societeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AffaireSocieteAdj id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffaireId() {
        return this.affaireId;
    }

    public AffaireSocieteAdj affaireId(Long affaireId) {
        this.setAffaireId(affaireId);
        return this;
    }

    public void setAffaireId(Long affaireId) {
        this.affaireId = affaireId;
    }

    public Long getSocieteId() {
        return this.societeId;
    }

    public AffaireSocieteAdj societeId(Long societeId) {
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
        if (!(o instanceof AffaireSocieteAdj)) {
            return false;
        }
        return id != null && id.equals(((AffaireSocieteAdj) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffaireSocieteAdj{" +
            "id=" + getId() +
            ", affaireId=" + getAffaireId() +
            ", societeId=" + getSocieteId() +
            "}";
    }
}
