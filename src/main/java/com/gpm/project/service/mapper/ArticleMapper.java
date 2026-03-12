package com.gpm.project.service.mapper;

import com.gpm.project.domain.Article;
import com.gpm.project.service.dto.ArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {}
