package com.gpm.project.service.mapper;

import com.gpm.project.domain.ContactSociete;
import com.gpm.project.service.dto.ContactSocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactSociete} and its DTO {@link ContactSocieteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactSocieteMapper extends EntityMapper<ContactSocieteDTO, ContactSociete> {}
