package com.gpm.project.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.PieceJointe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PieceJointeDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomFichier;

    @NotNull
    private String type;

    @NotNull
    private String fichierURL;

    @NotNull
    private ZonedDateTime dateUpload;

    /**
     * Cross-service FK → WorkOrder (operationsService)
     */
    @Schema(description = "Cross-service FK → WorkOrder (operationsService)")
    private Long workOrderId;

    private AffaireDTO affaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFichierURL() {
        return fichierURL;
    }

    public void setFichierURL(String fichierURL) {
        this.fichierURL = fichierURL;
    }

    public ZonedDateTime getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(ZonedDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Long getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public AffaireDTO getAffaire() {
        return affaire;
    }

    public void setAffaire(AffaireDTO affaire) {
        this.affaire = affaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PieceJointeDTO)) {
            return false;
        }

        PieceJointeDTO pieceJointeDTO = (PieceJointeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pieceJointeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointeDTO{" +
            "id=" + getId() +
            ", nomFichier='" + getNomFichier() + "'" +
            ", type='" + getType() + "'" +
            ", fichierURL='" + getFichierURL() + "'" +
            ", dateUpload='" + getDateUpload() + "'" +
            ", workOrderId=" + getWorkOrderId() +
            ", affaire=" + getAffaire() +
            "}";
    }
}
