package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MatriceJourFerie.
 */
@Entity
@Table(name = "matrice_jour_ferie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriceJourFerie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tarif_applique", nullable = false)
    private Float tarifApplique;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "affaire", "ville", "zone" }, allowSetters = true)
    private MatriceFacturation matrice;

    @ManyToOne(optional = false)
    @NotNull
    private JourFerie jourFerie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MatriceJourFerie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTarifApplique() {
        return this.tarifApplique;
    }

    public MatriceJourFerie tarifApplique(Float tarifApplique) {
        this.setTarifApplique(tarifApplique);
        return this;
    }

    public void setTarifApplique(Float tarifApplique) {
        this.tarifApplique = tarifApplique;
    }

    public MatriceFacturation getMatrice() {
        return this.matrice;
    }

    public void setMatrice(MatriceFacturation matriceFacturation) {
        this.matrice = matriceFacturation;
    }

    public MatriceJourFerie matrice(MatriceFacturation matriceFacturation) {
        this.setMatrice(matriceFacturation);
        return this;
    }

    public JourFerie getJourFerie() {
        return this.jourFerie;
    }

    public void setJourFerie(JourFerie jourFerie) {
        this.jourFerie = jourFerie;
    }

    public MatriceJourFerie jourFerie(JourFerie jourFerie) {
        this.setJourFerie(jourFerie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatriceJourFerie)) {
            return false;
        }
        return id != null && id.equals(((MatriceJourFerie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriceJourFerie{" +
            "id=" + getId() +
            ", tarifApplique=" + getTarifApplique() +
            "}";
    }
}
