package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AclEntryProjet.
 */
@Entity
@Table(name = "acl_entry_projet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AclEntryProjet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "project_id")
    private Long project_id;

    @Column(name = "sid_id")
    private Long sid_id;

    @Column(name = "sid")
    private String sid;

    @Column(name = "can_read")
    private Boolean can_read;

    @Column(name = "can_write")
    private Boolean can_write;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AclEntryProjet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProject_id() {
        return this.project_id;
    }

    public AclEntryProjet project_id(Long project_id) {
        this.setProject_id(project_id);
        return this;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public Long getSid_id() {
        return this.sid_id;
    }

    public AclEntryProjet sid_id(Long sid_id) {
        this.setSid_id(sid_id);
        return this;
    }

    public void setSid_id(Long sid_id) {
        this.sid_id = sid_id;
    }

    public String getSid() {
        return this.sid;
    }

    public AclEntryProjet sid(String sid) {
        this.setSid(sid);
        return this;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Boolean getCan_read() {
        return this.can_read;
    }

    public AclEntryProjet can_read(Boolean can_read) {
        this.setCan_read(can_read);
        return this;
    }

    public void setCan_read(Boolean can_read) {
        this.can_read = can_read;
    }

    public Boolean getCan_write() {
        return this.can_write;
    }

    public AclEntryProjet can_write(Boolean can_write) {
        this.setCan_write(can_write);
        return this;
    }

    public void setCan_write(Boolean can_write) {
        this.can_write = can_write;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AclEntryProjet)) {
            return false;
        }
        return id != null && id.equals(((AclEntryProjet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AclEntryProjet{" +
            "id=" + getId() +
            ", project_id=" + getProject_id() +
            ", sid_id=" + getSid_id() +
            ", sid='" + getSid() + "'" +
            ", can_read='" + getCan_read() + "'" +
            ", can_write='" + getCan_write() + "'" +
            "}";
    }
}
