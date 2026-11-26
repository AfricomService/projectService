package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RapportTemplateAffaire.
 */
@Entity
@Table(name = "rapport_template_affaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RapportTemplateAffaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "affaire_id")
    private Long affaireId;

    @Column(name = "rapport_template_id")
    private Long rapportTemplateId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RapportTemplateAffaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAffaireId() {
        return this.affaireId;
    }

    public RapportTemplateAffaire affaireId(Long affaireId) {
        this.setAffaireId(affaireId);
        return this;
    }

    public void setAffaireId(Long affaireId) {
        this.affaireId = affaireId;
    }

    public Long getRapportTemplateId() {
        return this.rapportTemplateId;
    }

    public RapportTemplateAffaire rapportTemplateId(Long rapportTemplateId) {
        this.setRapportTemplateId(rapportTemplateId);
        return this;
    }

    public void setRapportTemplateId(Long rapportTemplateId) {
        this.rapportTemplateId = rapportTemplateId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportTemplateAffaire)) {
            return false;
        }
        return id != null && id.equals(((RapportTemplateAffaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportTemplateAffaire{" +
            "id=" + getId() +
            ", affaireId=" + getAffaireId() +
            ", rapportTemplateId=" + getRapportTemplateId() +
            "}";
    }
}
