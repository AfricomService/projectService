package com.gpm.project.service.dto;

import com.gpm.project.domain.enumeration.StatutAffaire;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.Affaire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffaireDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer numAffaire;

    @NotNull
    private String designationAffaire;

    private String bonDeCommande;

    private Float montant;

    private String devise;

    private LocalDate dateDebut;

    private LocalDate dateCloture;

    private LocalDate datePassageExecution;

    private Boolean lieuMultipleParMission;

    private Float montantVente;

    private Float montantBudgetaireMateriel;

    private Float montantBudgetaireService;

    @NotNull
    private StatutAffaire statut;

    /**
     * Cross-service FK → Utilisateur (operationsService)
     */
    @Schema(description = "Cross-service FK → Utilisateur (operationsService)")
    private String responsableProjetId;

    private String responsableProjetUserLogin;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String createdBy;

    private String createdByUserLogin;

    private String updatedBy;

    private String updatedByUserLogin;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumAffaire() {
        return numAffaire;
    }

    public void setNumAffaire(Integer numAffaire) {
        this.numAffaire = numAffaire;
    }

    public String getDesignationAffaire() {
        return designationAffaire;
    }

    public void setDesignationAffaire(String designationAffaire) {
        this.designationAffaire = designationAffaire;
    }

    public String getBonDeCommande() {
        return bonDeCommande;
    }

    public void setBonDeCommande(String bonDeCommande) {
        this.bonDeCommande = bonDeCommande;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public LocalDate getDatePassageExecution() {
        return datePassageExecution;
    }

    public void setDatePassageExecution(LocalDate datePassageExecution) {
        this.datePassageExecution = datePassageExecution;
    }

    public Boolean getLieuMultipleParMission() {
        return lieuMultipleParMission;
    }

    public void setLieuMultipleParMission(Boolean lieuMultipleParMission) {
        this.lieuMultipleParMission = lieuMultipleParMission;
    }

    public Float getMontantVente() {
        return montantVente;
    }

    public void setMontantVente(Float montantVente) {
        this.montantVente = montantVente;
    }

    public Float getMontantBudgetaireMateriel() {
        return montantBudgetaireMateriel;
    }

    public void setMontantBudgetaireMateriel(Float montantBudgetaireMateriel) {
        this.montantBudgetaireMateriel = montantBudgetaireMateriel;
    }

    public Float getMontantBudgetaireService() {
        return montantBudgetaireService;
    }

    public void setMontantBudgetaireService(Float montantBudgetaireService) {
        this.montantBudgetaireService = montantBudgetaireService;
    }

    public StatutAffaire getStatut() {
        return statut;
    }

    public void setStatut(StatutAffaire statut) {
        this.statut = statut;
    }

    public String getResponsableProjetId() {
        return responsableProjetId;
    }

    public void setResponsableProjetId(String responsableProjetId) {
        this.responsableProjetId = responsableProjetId;
    }

    public String getResponsableProjetUserLogin() {
        return responsableProjetUserLogin;
    }

    public void setResponsableProjetUserLogin(String responsableProjetUserLogin) {
        this.responsableProjetUserLogin = responsableProjetUserLogin;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return createdByUserLogin;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return updatedByUserLogin;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffaireDTO)) {
            return false;
        }

        AffaireDTO affaireDTO = (AffaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffaireDTO{" +
            "id=" + getId() +
            ", numAffaire=" + getNumAffaire() +
            ", designationAffaire='" + getDesignationAffaire() + "'" +
            ", bonDeCommande='" + getBonDeCommande() + "'" +
            ", montant=" + getMontant() +
            ", devise='" + getDevise() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            ", datePassageExecution='" + getDatePassageExecution() + "'" +
            ", lieuMultipleParMission='" + getLieuMultipleParMission() + "'" +
            ", montantVente=" + getMontantVente() +
            ", montantBudgetaireMateriel=" + getMontantBudgetaireMateriel() +
            ", montantBudgetaireService=" + getMontantBudgetaireService() +
            ", statut='" + getStatut() + "'" +
            ", responsableProjetId='" + getResponsableProjetId() + "'" +
            ", responsableProjetUserLogin='" + getResponsableProjetUserLogin() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdByUserLogin='" + getCreatedByUserLogin() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedByUserLogin='" + getUpdatedByUserLogin() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
