package com.gpm.project.service.mapper;

import com.gpm.project.domain.TypeRessourceDetails;
import com.gpm.project.service.dto.TypeRessourceDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeRessourceDetails} and its DTO {@link TypeRessourceDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeRessourceDetailsMapper extends EntityMapper<TypeRessourceDetailsDTO, TypeRessourceDetails> {}
