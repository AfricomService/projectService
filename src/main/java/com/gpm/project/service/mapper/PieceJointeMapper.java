package com.gpm.project.service.mapper;

import com.gpm.project.domain.Affaire;
import com.gpm.project.domain.PieceJointe;
import com.gpm.project.service.dto.AffaireDTO;
import com.gpm.project.service.dto.PieceJointeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PieceJointe} and its DTO {@link PieceJointeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PieceJointeMapper extends EntityMapper<PieceJointeDTO, PieceJointe> {
    @Mapping(target = "affaire", source = "affaire", qualifiedByName = "affaireDesignationAffaire")
    PieceJointeDTO toDto(PieceJointe s);

    @Named("affaireDesignationAffaire")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designationAffaire", source = "designationAffaire")
    AffaireDTO toDtoAffaireDesignationAffaire(Affaire affaire);
}
