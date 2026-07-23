package com.gpm.project.service.mapper;

import com.gpm.project.domain.ParentClassdef;
import com.gpm.project.service.dto.ParentClassdefDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParentClassdef} and its DTO {@link ParentClassdefDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParentClassdefMapper extends EntityMapper<ParentClassdefDTO, ParentClassdef> {}
