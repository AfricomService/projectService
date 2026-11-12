package com.gpm.project.service.dto;

import java.io.Serializable;

/**
 * DTO léger pour l'affichage en liste des sites (id, code, désignation uniquement).
 */
public class SiteListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String designation;

    public SiteListDTO(Long id, String code, String designation) {
        this.id = id;
        this.code = code;
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDesignation() {
        return designation;
    }
}
