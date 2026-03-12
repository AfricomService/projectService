package com.gpm.project.service.mapper;

import com.gpm.project.domain.Ville;
import com.gpm.project.service.dto.VilleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ville} and its DTO {@link VilleDTO}.
 */
@Mapper(componentModel = "spring")
public interface VilleMapper extends EntityMapper<VilleDTO, Ville> {}
