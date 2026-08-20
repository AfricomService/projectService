package com.gpm.project.service.mapper;

import com.gpm.project.domain.TypeRessource;
import com.gpm.project.service.dto.TypeRessourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeRessource} and its DTO {@link TypeRessourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeRessourceMapper extends EntityMapper<TypeRessourceDTO, TypeRessource> {}
