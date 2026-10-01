package com.gpm.project.domain;

import com.gpm.project.domain.enumeration.SidType;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import javax.persistence.*;

/**
 * A security identity (a user or a role).
 *
 * Identity is the canonical {@code sidKey} ("USER:john.doe" / "ROLE:ROLE_MANAGER"),
 * which is unique in the database. Type is part of the key, so a user named
 * "ROLE_ADMIN" can never match a role entry.
 *
 * No second-level cache on purpose: permission changes must be visible immediately
 * on every node.
 */
@Entity
@Table(name = "acl_sid")
public class AclSid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sid_type", nullable = false)
    private SidType sidType;

    @Column(name = "sid_value", nullable = false)
    private String sidValue;

    @Column(name = "sid_key", nullable = false, unique = true, length = 300)
    private String sidKey;

    @Column(name = "nom_descriptif")
    private String nomDescriptif;

    /** Canonical key used for every permission lookup. Usernames are case-insensitive, roles are not. */
    public static String buildKey(SidType type, String value) {
        Objects.requireNonNull(type, "sidType");
        Objects.requireNonNull(value, "sidValue");
        String v = value.trim();
        if (type == SidType.USER) {
            v = v.toLowerCase(Locale.ROOT);
        }
        return type.name() + ":" + v;
    }

    @PrePersist
    @PreUpdate
    void computeSidKey() {
        this.sidKey = buildKey(sidType, sidValue);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SidType getSidType() {
        return sidType;
    }

    public void setSidType(SidType sidType) {
        this.sidType = sidType;
    }

    public String getSidValue() {
        return sidValue;
    }

    public void setSidValue(String sidValue) {
        this.sidValue = sidValue;
    }

    public String getSidKey() {
        return sidKey;
    }

    public String getNomDescriptif() {
        return nomDescriptif;
    }

    public void setNomDescriptif(String nomDescriptif) {
        this.nomDescriptif = nomDescriptif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclSid)) {
            return false;
        }
        return id != null && id.equals(((AclSid) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AclSid{id=" + id + ", sidKey='" + sidKey + "', nomDescriptif='" + nomDescriptif + "'}";
    }
}
