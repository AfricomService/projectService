package com.gpm.project.service.mapper;

import com.gpm.project.domain.Ville;
import com.gpm.project.domain.Zone;
import com.gpm.project.service.dto.VilleDTO;
import com.gpm.project.service.dto.ZoneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Zone} and its DTO {@link ZoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface ZoneMapper extends EntityMapper<ZoneDTO, Zone> {
    @Mapping(target = "ville", source = "ville", qualifiedByName = "villeNom")
    ZoneDTO toDto(Zone s);

    @Named("villeNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    VilleDTO toDtoVilleNom(Ville ville);
}
