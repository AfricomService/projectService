package com.gpm.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AffaireArticle.
 */
@Entity
@Table(name = "affaire_article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffaireArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantite_contractuelle")
    private Float quantiteContractuelle;

    @Column(name = "quantite_realisee")
    private Float quantiteRealisee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "client" }, allowSetters = true)
    private Affaire affaire;

    @ManyToOne(optional = false)
    @NotNull
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AffaireArticle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantiteContractuelle() {
        return this.quantiteContractuelle;
    }

    public AffaireArticle quantiteContractuelle(Float quantiteContractuelle) {
        this.setQuantiteContractuelle(quantiteContractuelle);
        return this;
    }

    public void setQuantiteContractuelle(Float quantiteContractuelle) {
        this.quantiteContractuelle = quantiteContractuelle;
    }

    public Float getQuantiteRealisee() {
        return this.quantiteRealisee;
    }

    public AffaireArticle quantiteRealisee(Float quantiteRealisee) {
        this.setQuantiteRealisee(quantiteRealisee);
        return this;
    }

    public void setQuantiteRealisee(Float quantiteRealisee) {
        this.quantiteRealisee = quantiteRealisee;
    }

    public Affaire getAffaire() {
        return this.affaire;
    }

    public void setAffaire(Affaire affaire) {
        this.affaire = affaire;
    }

    public AffaireArticle affaire(Affaire affaire) {
        this.setAffaire(affaire);
        return this;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public AffaireArticle article(Article article) {
        this.setArticle(article);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffaireArticle)) {
            return false;
        }
        return id != null && id.equals(((AffaireArticle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffaireArticle{" +
            "id=" + getId() +
            ", quantiteContractuelle=" + getQuantiteContractuelle() +
            ", quantiteRealisee=" + getQuantiteRealisee() +
            "}";
    }
}
