package com.gpm.project.service.mapper;

import com.gpm.project.domain.UserAuthSociete;
import com.gpm.project.service.dto.UserAuthSocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAuthSociete} and its DTO {@link UserAuthSocieteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAuthSocieteMapper extends EntityMapper<UserAuthSocieteDTO, UserAuthSociete> {}
