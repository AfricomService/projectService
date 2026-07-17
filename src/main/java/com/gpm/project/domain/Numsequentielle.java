package com.gpm.project.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Numsequentielle.
 */
@Entity
@Table(name = "numsequentielle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Numsequentielle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "prefix")
    private String prefix;

    @NotNull
    @Column(name = "next_number", nullable = false)
    private String nextNumber;

    @Column(name = "format")
    private String format;

    @Column(name = "code_num_seq", unique = true)
    private String codeNumSeq;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Numsequentielle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Numsequentielle prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNextNumber() {
        return this.nextNumber;
    }

    public Numsequentielle nextNumber(String nextNumber) {
        this.setNextNumber(nextNumber);
        return this;
    }

    public void setNextNumber(String nextNumber) {
        this.nextNumber = nextNumber;
    }

    public String getFormat() {
        return this.format;
    }

    public Numsequentielle format(String format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCodeNumSeq() {
        return this.codeNumSeq;
    }

    public Numsequentielle codeNumSeq(String codeNumSeq) {
        this.setCodeNumSeq(codeNumSeq);
        return this;
    }

    public void setCodeNumSeq(String codeNumSeq) {
        this.codeNumSeq = codeNumSeq;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Numsequentielle)) {
            return false;
        }
        return id != null && id.equals(((Numsequentielle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Numsequentielle{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", nextNumber='" + getNextNumber() + "'" +
            ", format='" + getFormat() + "'" +
            ", codeNumSeq='" + getCodeNumSeq() + "'" +
            "}";
    }
}
