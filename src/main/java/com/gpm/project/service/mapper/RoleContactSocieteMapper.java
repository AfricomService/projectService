package com.gpm.project.service.mapper;

import com.gpm.project.domain.RoleContactSociete;
import com.gpm.project.service.dto.RoleContactSocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoleContactSociete} and its DTO {@link RoleContactSocieteDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleContactSocieteMapper extends EntityMapper<RoleContactSocieteDTO, RoleContactSociete> {}
