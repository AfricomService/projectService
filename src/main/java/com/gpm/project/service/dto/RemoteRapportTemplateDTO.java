package com.gpm.project.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteRapportTemplateDTO {

    private Long id;
    private String name;
    private String description;
    private Integer delayToArchive;
    private Integer delaiDeTraitement;
    private List<String> email;
    private Boolean canBeCreatedByTech;
    private Boolean actif;
    private Integer classement;
    private Long projetId;
    private Boolean hasTemplateExcelFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDelayToArchive() {
        return delayToArchive;
    }

    public void setDelayToArchive(Integer delayToArchive) {
        this.delayToArchive = delayToArchive;
    }

    public Integer getDelaiDeTraitement() {
        return delaiDeTraitement;
    }

    public void setDelaiDeTraitement(Integer delaiDeTraitement) {
        this.delaiDeTraitement = delaiDeTraitement;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public Boolean getCanBeCreatedByTech() {
        return canBeCreatedByTech;
    }

    public void setCanBeCreatedByTech(Boolean canBeCreatedByTech) {
        this.canBeCreatedByTech = canBeCreatedByTech;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Integer getClassement() {
        return classement;
    }

    public void setClassement(Integer classement) {
        this.classement = classement;
    }

    public Long getProjetId() {
        return projetId;
    }

    public void setProjetId(Long projetId) {
        this.projetId = projetId;
    }

    public Boolean getHasTemplateExcelFile() {
        return hasTemplateExcelFile;
    }

    public void setHasTemplateExcelFile(Boolean hasTemplateExcelFile) {
        this.hasTemplateExcelFile = hasTemplateExcelFile;
    }
}
