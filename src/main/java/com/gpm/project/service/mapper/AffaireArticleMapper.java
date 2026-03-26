package com.gpm.project.service.mapper;

import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.AffaireArticle;
import com.gpm.project.domain.Article;
import com.gpm.project.service.dto.AffaireArticleDTO;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.dto.ArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AffaireArticle} and its DTO {@link AffaireArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffaireArticleMapper extends EntityMapper<AffaireArticleDTO, AffaireArticle> {
    @Mapping(target = "affaire", source = "affaire", qualifiedByName = "affaireDesignationAffaire")
    @Mapping(target = "article", source = "article", qualifiedByName = "articleCode")
    AffaireArticleDTO toDto(AffaireArticle s);

    @Named("affaireDesignationAffaire")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designationAffaire", source = "designationAffaire")
    AffaireDTO toDtoAffaireDesignationAffaire(Affaire affaire);

    @Named("articleCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "designation", source = "designation")
    @Mapping(target = "uniteMesure", source = "uniteMesure")
    ArticleDTO toDtoArticleCode(Article article);
}
