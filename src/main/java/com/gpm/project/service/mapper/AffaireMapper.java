package com.gpm.project.service.mapper;

import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.Client;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Affaire} and its DTO {@link AffaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffaireMapper extends EntityMapper<AffaireDTO, Affaire> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientRaisonSociale")
    AffaireDTO toDto(Affaire s);

    @Named("clientRaisonSociale")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "raisonSociale", source = "raisonSociale")
    ClientDTO toDtoClientRaisonSociale(Client client);
}
