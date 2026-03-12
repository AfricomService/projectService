package com.gpm.project.service.mapper;

import com.gpm.project.domain.JourFerie;
import com.gpm.project.service.dto.JourFerieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JourFerie} and its DTO {@link JourFerieDTO}.
 */
@Mapper(componentModel = "spring")
public interface JourFerieMapper extends EntityMapper<JourFerieDTO, JourFerie> {}
