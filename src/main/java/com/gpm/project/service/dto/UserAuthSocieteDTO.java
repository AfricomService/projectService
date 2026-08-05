package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.gpm.project.domain.UserAuthSociete} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAuthSocieteDTO implements Serializable {

    private Long id;

    private Long societeId;

    private Long roleContactSocieteId;

    private Long contactSocieteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public Long getRoleContactSocieteId() {
        return roleContactSocieteId;
    }

    public void setRoleContactSocieteId(Long roleContactSocieteId) {
        this.roleContactSocieteId = roleContactSocieteId;
    }

    public Long getContactSocieteId() {
        return contactSocieteId;
    }

    public void setContactSocieteId(Long contactSocieteId) {
        this.contactSocieteId = contactSocieteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAuthSocieteDTO)) {
            return false;
        }

        UserAuthSocieteDTO userAuthSocieteDTO = (UserAuthSocieteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAuthSocieteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAuthSocieteDTO{" +
            "id=" + getId() +
            ", societeId=" + getSocieteId() +
            ", roleContactSocieteId=" + getRoleContactSocieteId() +
            ", contactSocieteId=" + getContactSocieteId() +
            "}";
    }
}
