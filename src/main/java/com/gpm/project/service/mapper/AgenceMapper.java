package com.gpm.project.service.mapper;

import com.gpm.project.domain.Agence;
import com.gpm.project.domain.Societe;
import com.gpm.project.service.dto.AgenceDTO;
import com.gpm.project.service.dto.SocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agence} and its DTO {@link AgenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgenceMapper extends EntityMapper<AgenceDTO, Agence> {
    @Mapping(target = "societe", source = "societe", qualifiedByName = "societeRaisonSociale")
    AgenceDTO toDto(Agence s);

    @Named("societeRaisonSociale")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "raisonSociale", source = "raisonSociale")
    SocieteDTO toDtoSocieteRaisonSociale(Societe societe);
}
