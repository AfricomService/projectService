package com.gpm.project.service.mapper;

import com.gpm.project.domain.Ressource;
import com.gpm.project.service.dto.RessourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ressource} and its DTO {@link RessourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RessourceMapper extends EntityMapper<RessourceDTO, Ressource> {}
