package com.gpm.project.service.mapper;

import com.gpm.project.domain.Client;
import com.gpm.project.domain.Contact;
import com.gpm.project.service.dto.ClientDTO;
import com.gpm.project.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientRaisonSociale")
    ContactDTO toDto(Contact s);

    @Named("clientRaisonSociale")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "raisonSociale", source = "raisonSociale")
    ClientDTO toDtoClientRaisonSociale(Client client);
}
