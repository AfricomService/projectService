package com.gpm.project.service.mapper;

import com.gpm.project.domain.Agence;
import com.gpm.project.domain.Vehicule;
import com.gpm.project.service.dto.AgenceDTO;
import com.gpm.project.service.dto.VehiculeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vehicule} and its DTO {@link VehiculeDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehiculeMapper extends EntityMapper<VehiculeDTO, Vehicule> {
    @Mapping(target = "agence", source = "agence", qualifiedByName = "agenceDesignation")
    VehiculeDTO toDto(Vehicule s);

    @Named("agenceDesignation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    AgenceDTO toDtoAgenceDesignation(Agence agence);
}
