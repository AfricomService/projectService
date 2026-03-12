package com.gpm.project.service.mapper;

import com.gpm.project.domain.Societe;
import com.gpm.project.service.dto.SocieteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Societe} and its DTO {@link SocieteDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocieteMapper extends EntityMapper<SocieteDTO, Societe> {}
