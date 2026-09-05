package com.gpm.project.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

/**
 * DTO utilisé pour renommer une PieceJointe (champ nomFichier uniquement).
 */
public class PieceJointeRenameDTO implements Serializable {

    @NotBlank
    private String nomFichier;

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }
}
