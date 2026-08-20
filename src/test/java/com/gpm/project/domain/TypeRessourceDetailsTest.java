package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeRessourceDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRessourceDetails.class);
        TypeRessourceDetails typeRessourceDetails1 = new TypeRessourceDetails();
        typeRessourceDetails1.setId(1L);
        TypeRessourceDetails typeRessourceDetails2 = new TypeRessourceDetails();
        typeRessourceDetails2.setId(typeRessourceDetails1.getId());
        assertThat(typeRessourceDetails1).isEqualTo(typeRessourceDetails2);
        typeRessourceDetails2.setId(2L);
        assertThat(typeRessourceDetails1).isNotEqualTo(typeRessourceDetails2);
        typeRessourceDetails1.setId(null);
        assertThat(typeRessourceDetails1).isNotEqualTo(typeRessourceDetails2);
    }
}
