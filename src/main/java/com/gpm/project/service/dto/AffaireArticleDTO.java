package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.AffaireArticle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AffaireArticleDTO implements Serializable {

    private Long id;

    private Float quantiteContractuelle;

    private Float quantiteRealisee;

    private AffaireDTO affaire;

    private ArticleDTO article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getQuantiteContractuelle() {
        return quantiteContractuelle;
    }

    public void setQuantiteContractuelle(Float quantiteContractuelle) {
        this.quantiteContractuelle = quantiteContractuelle;
    }

    public Float getQuantiteRealisee() {
        return quantiteRealisee;
    }

    public void setQuantiteRealisee(Float quantiteRealisee) {
        this.quantiteRealisee = quantiteRealisee;
    }

    public AffaireDTO getAffaire() {
        return affaire;
    }

    public void setAffaire(AffaireDTO affaire) {
        this.affaire = affaire;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AffaireArticleDTO)) {
            return false;
        }

        AffaireArticleDTO affaireArticleDTO = (AffaireArticleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, affaireArticleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AffaireArticleDTO{" +
            "id=" + getId() +
            ", quantiteContractuelle=" + getQuantiteContractuelle() +
            ", quantiteRealisee=" + getQuantiteRealisee() +
            ", affaire=" + getAffaire() +
            ", article=" + getArticle() +
            "}";
    }
}
