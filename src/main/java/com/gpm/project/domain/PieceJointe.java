package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PieceJointe.
 */
@Entity
@Table(name = "piece_jointe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PieceJointe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_fichier", nullable = false)
    private String nomFichier;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "fichier_url", nullable = false)
    private String fichierURL;

    @NotNull
    @Column(name = "date_upload", nullable = false)
    private ZonedDateTime dateUpload;

    /**
     * Cross-service FK → WorkOrder (operationsService)
     */
    @Column(name = "work_order_id")
    private Long workOrderId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Affaire affaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PieceJointe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomFichier() {
        return this.nomFichier;
    }

    public PieceJointe nomFichier(String nomFichier) {
        this.setNomFichier(nomFichier);
        return this;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getType() {
        return this.type;
    }

    public PieceJointe type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFichierURL() {
        return this.fichierURL;
    }

    public PieceJointe fichierURL(String fichierURL) {
        this.setFichierURL(fichierURL);
        return this;
    }

    public void setFichierURL(String fichierURL) {
        this.fichierURL = fichierURL;
    }

    public ZonedDateTime getDateUpload() {
        return this.dateUpload;
    }

    public PieceJointe dateUpload(ZonedDateTime dateUpload) {
        this.setDateUpload(dateUpload);
        return this;
    }

    public void setDateUpload(ZonedDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    public Long getWorkOrderId() {
        return this.workOrderId;
    }

    public PieceJointe workOrderId(Long workOrderId) {
        this.setWorkOrderId(workOrderId);
        return this;
    }

    public void setWorkOrderId(Long workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Affaire getAffaire() {
        return this.affaire;
    }

    public void setAffaire(Affaire affaire) {
        this.affaire = affaire;
    }

    public PieceJointe affaire(Affaire affaire) {
        this.setAffaire(affaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PieceJointe)) {
            return false;
        }
        return id != null && id.equals(((PieceJointe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PieceJointe{" +
            "id=" + getId() +
            ", nomFichier='" + getNomFichier() + "'" +
            ", type='" + getType() + "'" +
            ", fichierURL='" + getFichierURL() + "'" +
            ", dateUpload='" + getDateUpload() + "'" +
            ", workOrderId=" + getWorkOrderId() +
            "}";
    }
}
