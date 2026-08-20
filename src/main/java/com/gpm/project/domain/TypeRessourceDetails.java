package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeRessourceDetails.
 */
@Entity
@Table(name = "type_ressource_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeRessourceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type_ressource_id")
    private Long typeRessourceId;

    @Column(name = "detail_ressource_id")
    private Long detailRessourceId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeRessourceDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeRessourceId() {
        return this.typeRessourceId;
    }

    public TypeRessourceDetails typeRessourceId(Long typeRessourceId) {
        this.setTypeRessourceId(typeRessourceId);
        return this;
    }

    public void setTypeRessourceId(Long typeRessourceId) {
        this.typeRessourceId = typeRessourceId;
    }

    public Long getDetailRessourceId() {
        return this.detailRessourceId;
    }

    public TypeRessourceDetails detailRessourceId(Long detailRessourceId) {
        this.setDetailRessourceId(detailRessourceId);
        return this;
    }

    public void setDetailRessourceId(Long detailRessourceId) {
        this.detailRessourceId = detailRessourceId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeRessourceDetails)) {
            return false;
        }
        return id != null && id.equals(((TypeRessourceDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeRessourceDetails{" +
            "id=" + getId() +
            ", typeRessourceId=" + getTypeRessourceId() +
            ", detailRessourceId=" + getDetailRessourceId() +
            "}";
    }
}
