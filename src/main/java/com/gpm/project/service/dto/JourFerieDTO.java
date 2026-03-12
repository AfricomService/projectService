package com.gpm.project.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.JourFerie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JourFerieDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String designation;

    @NotNull
    private Integer annee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JourFerieDTO)) {
            return false;
        }

        JourFerieDTO jourFerieDTO = (JourFerieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, jourFerieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JourFerieDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", annee=" + getAnnee() +
            "}";
    }
}
