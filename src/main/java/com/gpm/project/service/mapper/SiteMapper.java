package com.gpm.project.service.mapper;

import com.gpm.project.domain.Client;
import com.gpm.project.domain.Site;
import com.gpm.project.domain.Ville;
import com.gpm.project.service.dto.ClientDTO;
import com.gpm.project.service.dto.SiteDTO;
import com.gpm.project.service.dto.VilleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Site} and its DTO {@link SiteDTO}.
 */
@Mapper(componentModel = "spring")
public interface SiteMapper extends EntityMapper<SiteDTO, Site> {
    @Mapping(target = "ville", source = "ville", qualifiedByName = "villeNom")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientRaisonSociale")
    SiteDTO toDto(Site s);

    @Named("villeNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    VilleDTO toDtoVilleNom(Ville ville);

    @Named("clientRaisonSociale")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "raisonSociale", source = "raisonSociale")
    ClientDTO toDtoClientRaisonSociale(Client client);
}
