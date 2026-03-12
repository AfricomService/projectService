package com.gpm.project.service.mapper;

import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.MatriceFacturation;
import com.gpm.project.domain.Ville;
import com.gpm.project.domain.Zone;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.dto.MatriceFacturationDTO;
import com.gpm.project.service.dto.VilleDTO;
import com.gpm.project.service.dto.ZoneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MatriceFacturation} and its DTO {@link MatriceFacturationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatriceFacturationMapper extends EntityMapper<MatriceFacturationDTO, MatriceFacturation> {
    @Mapping(target = "affaire", source = "affaire", qualifiedByName = "affaireDesignationAffaire")
    @Mapping(target = "ville", source = "ville", qualifiedByName = "villeNom")
    @Mapping(target = "zone", source = "zone", qualifiedByName = "zoneNom")
    MatriceFacturationDTO toDto(MatriceFacturation s);

    @Named("affaireDesignationAffaire")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designationAffaire", source = "designationAffaire")
    AffaireDTO toDtoAffaireDesignationAffaire(Affaire affaire);

    @Named("villeNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    VilleDTO toDtoVilleNom(Ville ville);

    @Named("zoneNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    ZoneDTO toDtoZoneNom(Zone zone);
}
