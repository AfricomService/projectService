package com.gpm.project.service.mapper;

import com.gpm.project.domain.Numsequentielle;
import com.gpm.project.service.dto.NumsequentielleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Numsequentielle} and its DTO {@link NumsequentielleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NumsequentielleMapper extends EntityMapper<NumsequentielleDTO, Numsequentielle> {}
