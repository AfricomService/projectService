package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gpm.project.domain.enumeration.StatutAffaire;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Affaire.
 */
@Entity
@Table(name = "affaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Affaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "num_affaire", nullable = false, unique = true)
    private Integer numAffaire;

    @NotNull
    @Column(name = "designation_affaire", nullable = false)
    private String designationAffaire;

    @Column(name = "bon_de_commande")
    private String bonDeCommande;

    @Column(name = "montant")
    private Float montant;

    @Column(name = "devise")
    private String devise;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Column(name = "date_passage_execution")
    private LocalDate datePassageExecution;

    @Column(name = "lieu_multiple_par_mission")
    private Boolean lieuMultipleParMission;

    @Column(name = "montant_vente")
    private Float montantVente;

    @Column(name = "montant_budgetaire_materiel")
    private Float montantBudgetaireMateriel;

    @Column(name = "montant_budgetaire_service")
    private Float montantBudgetaireService;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutAffaire statut;

    /**
     * Cross-service FK → Utilisateur (operationsService)
     */
    @Column(name = "responsable_projet_id")
    private String responsableProjetId;

    @Column(name = "responsable_projet_user_login")
    private String responsableProjetUserLogin;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_by_user_login")
    private String createdByUserLogin;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_by_user_login")
    private String updatedByUserLogin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "contacts", "sites" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Affaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumAffaire() {
        return this.numAffaire;
    }

    public Affaire numAffaire(Integer numAffaire) {
        this.setNumAffaire(numAffaire);
        return this;
    }

    public void setNumAffaire(Integer numAffaire) {
        this.numAffaire = numAffaire;
    }

    public String getDesignationAffaire() {
        return this.designationAffaire;
    }

    public Affaire designationAffaire(String designationAffaire) {
        this.setDesignationAffaire(designationAffaire);
        return this;
    }

    public void setDesignationAffaire(String designationAffaire) {
        this.designationAffaire = designationAffaire;
    }

    public String getBonDeCommande() {
        return this.bonDeCommande;
    }

    public Affaire bonDeCommande(String bonDeCommande) {
        this.setBonDeCommande(bonDeCommande);
        return this;
    }

    public void setBonDeCommande(String bonDeCommande) {
        this.bonDeCommande = bonDeCommande;
    }

    public Float getMontant() {
        return this.montant;
    }

    public Affaire montant(Float montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getDevise() {
        return this.devise;
    }

    public Affaire devise(String devise) {
        this.setDevise(devise);
        return this;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Affaire dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateCloture() {
        return this.dateCloture;
    }

    public Affaire dateCloture(LocalDate dateCloture) {
        this.setDateCloture(dateCloture);
        return this;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public LocalDate getDatePassageExecution() {
        return this.datePassageExecution;
    }

    public Affaire datePassageExecution(LocalDate datePassageExecution) {
        this.setDatePassageExecution(datePassageExecution);
        return this;
    }

    public void setDatePassageExecution(LocalDate datePassageExecution) {
        this.datePassageExecution = datePassageExecution;
    }

    public Boolean getLieuMultipleParMission() {
        return this.lieuMultipleParMission;
    }

    public Affaire lieuMultipleParMission(Boolean lieuMultipleParMission) {
        this.setLieuMultipleParMission(lieuMultipleParMission);
        return this;
    }

    public void setLieuMultipleParMission(Boolean lieuMultipleParMission) {
        this.lieuMultipleParMission = lieuMultipleParMission;
    }

    public Float getMontantVente() {
        return this.montantVente;
    }

    public Affaire montantVente(Float montantVente) {
        this.setMontantVente(montantVente);
        return this;
    }

    public void setMontantVente(Float montantVente) {
        this.montantVente = montantVente;
    }

    public Float getMontantBudgetaireMateriel() {
        return this.montantBudgetaireMateriel;
    }

    public Affaire montantBudgetaireMateriel(Float montantBudgetaireMateriel) {
        this.setMontantBudgetaireMateriel(montantBudgetaireMateriel);
        return this;
    }

    public void setMontantBudgetaireMateriel(Float montantBudgetaireMateriel) {
        this.montantBudgetaireMateriel = montantBudgetaireMateriel;
    }

    public Float getMontantBudgetaireService() {
        return this.montantBudgetaireService;
    }

    public Affaire montantBudgetaireService(Float montantBudgetaireService) {
        this.setMontantBudgetaireService(montantBudgetaireService);
        return this;
    }

    public void setMontantBudgetaireService(Float montantBudgetaireService) {
        this.montantBudgetaireService = montantBudgetaireService;
    }

    public StatutAffaire getStatut() {
        return this.statut;
    }

    public Affaire statut(StatutAffaire statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutAffaire statut) {
        this.statut = statut;
    }

    public String getResponsableProjetId() {
        return this.responsableProjetId;
    }

    public Affaire responsableProjetId(String responsableProjetId) {
        this.setResponsableProjetId(responsableProjetId);
        return this;
    }

    public void setResponsableProjetId(String responsableProjetId) {
        this.responsableProjetId = responsableProjetId;
    }

    public String getResponsableProjetUserLogin() {
        return this.responsableProjetUserLogin;
    }

    public Affaire responsableProjetUserLogin(String responsableProjetUserLogin) {
        this.setResponsableProjetUserLogin(responsableProjetUserLogin);
        return this;
    }

    public void setResponsableProjetUserLogin(String responsableProjetUserLogin) {
        this.responsableProjetUserLogin = responsableProjetUserLogin;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Affaire createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Affaire updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Affaire createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return this.createdByUserLogin;
    }

    public Affaire createdByUserLogin(String createdByUserLogin) {
        this.setCreatedByUserLogin(createdByUserLogin);
        return this;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Affaire updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return this.updatedByUserLogin;
    }

    public Affaire updatedByUserLogin(String updatedByUserLogin) {
        this.setUpdatedByUserLogin(updatedByUserLogin);
        return this;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Affaire client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affaire)) {
            return false;
        }
        return id != null && id.equals(((Affaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affaire{" +
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
            "}";
    }
}
