package com.gpm.project.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

/**
 * A DetailRessource.
 */
@Entity
@Table(name = "detail_ressource")
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DetailRessource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "label")
    private String label;

    @Column(name = "code")
    private String code;

    @Column(name = "required")
    private Boolean required;

    @Column(name = "input_type")
    private String inputType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "multiple_choice_option")
    private List<Map<String, String>> multipleChoiceOption;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetailRessource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public DetailRessource status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLabel() {
        return this.label;
    }

    public DetailRessource label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return this.code;
    }

    public DetailRessource code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getRequired() {
        return this.required;
    }

    public DetailRessource required(Boolean required) {
        this.setRequired(required);
        return this;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getInputType() {
        return this.inputType;
    }

    public DetailRessource inputType(String inputType) {
        this.setInputType(inputType);
        return this;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public List<Map<String, String>> getMultipleChoiceOption() {
        return this.multipleChoiceOption;
    }

    public DetailRessource multipleChoiceOption(List<Map<String, String>> multipleChoiceOption) {
        this.setMultipleChoiceOption(multipleChoiceOption);
        return this;
    }

    public void setMultipleChoiceOption(List<Map<String, String>> multipleChoiceOption) {
        this.multipleChoiceOption = multipleChoiceOption;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailRessource)) {
            return false;
        }
        return id != null && id.equals(((DetailRessource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailRessource{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", label='" + getLabel() + "'" +
            ", code='" + getCode() + "'" +
            ", required='" + getRequired() + "'" +
            ", inputType='" + getInputType() + "'" +
            ", multipleChoiceOption='" + getMultipleChoiceOption() + "'" +
            "}";
    }
}
