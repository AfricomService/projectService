package com.gpm.project.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gpm.project.domain.Numsequentielle} entity.
 */
public class NumsequentielleDTO implements Serializable {

    private Long id;

    private String prefix;

    @NotNull
    private String nextNumber;

    private String format;

    private String codeNumSeq;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(String nextNumber) {
        this.nextNumber = nextNumber;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCodeNumSeq() {
        return codeNumSeq;
    }

    public void setCodeNumSeq(String codeNumSeq) {
        this.codeNumSeq = codeNumSeq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumsequentielleDTO)) {
            return false;
        }

        NumsequentielleDTO numsequentielleDTO = (NumsequentielleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, numsequentielleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NumsequentielleDTO{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", nextNumber='" + getNextNumber() + "'" +
            ", format='" + getFormat() + "'" +
            ", codeNumSeq='" + getCodeNumSeq() + "'" +
            "}";
    }
}
