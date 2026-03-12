package com.gpm.project.service.mapper;

import com.gpm.project.domain.JourFerie;
import com.gpm.project.domain.MatriceFacturation;
import com.gpm.project.domain.MatriceJourFerie;
import com.gpm.project.service.dto.JourFerieDTO;
import com.gpm.project.service.dto.MatriceFacturationDTO;
import com.gpm.project.service.dto.MatriceJourFerieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MatriceJourFerie} and its DTO {@link MatriceJourFerieDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatriceJourFerieMapper extends EntityMapper<MatriceJourFerieDTO, MatriceJourFerie> {
    @Mapping(target = "matrice", source = "matrice", qualifiedByName = "matriceFacturationId")
    @Mapping(target = "jourFerie", source = "jourFerie", qualifiedByName = "jourFerieDesignation")
    MatriceJourFerieDTO toDto(MatriceJourFerie s);

    @Named("matriceFacturationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MatriceFacturationDTO toDtoMatriceFacturationId(MatriceFacturation matriceFacturation);

    @Named("jourFerieDesignation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    JourFerieDTO toDtoJourFerieDesignation(JourFerie jourFerie);
}
