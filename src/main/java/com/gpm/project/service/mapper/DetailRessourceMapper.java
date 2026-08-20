package com.gpm.project.service.mapper;

import com.gpm.project.domain.DetailRessource;
import com.gpm.project.service.dto.DetailRessourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DetailRessource} and its DTO {@link DetailRessourceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetailRessourceMapper extends EntityMapper<DetailRessourceDTO, DetailRessource> {}
