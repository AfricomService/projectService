package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.MatriceJourFerie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatriceJourFerieDTO implements Serializable {

    private Long id;

    @NotNull
    private Float tarifApplique;

    private MatriceFacturationDTO matrice;

    private JourFerieDTO jourFerie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTarifApplique() {
        return tarifApplique;
    }

    public void setTarifApplique(Float tarifApplique) {
        this.tarifApplique = tarifApplique;
    }

    public MatriceFacturationDTO getMatrice() {
        return matrice;
    }

    public void setMatrice(MatriceFacturationDTO matrice) {
        this.matrice = matrice;
    }

    public JourFerieDTO getJourFerie() {
        return jourFerie;
    }

    public void setJourFerie(JourFerieDTO jourFerie) {
        this.jourFerie = jourFerie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatriceJourFerieDTO)) {
            return false;
        }

        MatriceJourFerieDTO matriceJourFerieDTO = (MatriceJourFerieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matriceJourFerieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatriceJourFerieDTO{" +
            "id=" + getId() +
            ", tarifApplique=" + getTarifApplique() +
            ", matrice=" + getMatrice() +
            ", jourFerie=" + getJourFerie() +
            "}";
    }
}
