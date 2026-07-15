package com.gpm.project.service.mapper;

import com.gpm.project.domain.AffaireSocieteAdj;
import com.gpm.project.service.dto.AffaireSocieteAdjDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AffaireSocieteAdj} and its DTO {@link AffaireSocieteAdjDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffaireSocieteAdjMapper extends EntityMapper<AffaireSocieteAdjDTO, AffaireSocieteAdj> {}
