package com.gpm.project.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.Site} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String designation;

    private Float gpsX;

    private Float gpsY;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String createdBy;

    private String createdByUserLogin;

    private String updatedBy;

    private String updatedByUserLogin;

    private VilleDTO ville;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Float getGpsX() {
        return gpsX;
    }

    public void setGpsX(Float gpsX) {
        this.gpsX = gpsX;
    }

    public Float getGpsY() {
        return gpsY;
    }

    public void setGpsY(Float gpsY) {
        this.gpsY = gpsY;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUserLogin() {
        return createdByUserLogin;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByUserLogin() {
        return updatedByUserLogin;
    }

    public void setUpdatedByUserLogin(String updatedByUserLogin) {
        this.updatedByUserLogin = updatedByUserLogin;
    }

    public VilleDTO getVille() {
        return ville;
    }

    public void setVille(VilleDTO ville) {
        this.ville = ville;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteDTO)) {
            return false;
        }

        SiteDTO siteDTO = (SiteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, siteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", gpsX=" + getGpsX() +
            ", gpsY=" + getGpsY() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdByUserLogin='" + getCreatedByUserLogin() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedByUserLogin='" + getUpdatedByUserLogin() + "'" +
            ", ville=" + getVille() +
            ", client=" + getClient() +
            "}";
    }
}
