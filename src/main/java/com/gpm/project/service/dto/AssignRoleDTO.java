package com.gpm.project.service.dto;

public class AssignRoleDTO {

    private Long societeId;

    private Long contactSocieteId;

    private Long roleContactSocieteId;

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public Long getContactSocieteId() {
        return contactSocieteId;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    public Long getRoleContactSocieteId() {
        return roleContactSocieteId;
    }

    public void setRoleContactSocieteId(Long roleContactSocieteId) {
        this.roleContactSocieteId = roleContactSocieteId;
    }
}
