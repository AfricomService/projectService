package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeRessourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRessourceDTO.class);
        TypeRessourceDTO typeRessourceDTO1 = new TypeRessourceDTO();
        typeRessourceDTO1.setId(1L);
        TypeRessourceDTO typeRessourceDTO2 = new TypeRessourceDTO();
        assertThat(typeRessourceDTO1).isNotEqualTo(typeRessourceDTO2);
        typeRessourceDTO2.setId(typeRessourceDTO1.getId());
        assertThat(typeRessourceDTO1).isEqualTo(typeRessourceDTO2);
        typeRessourceDTO2.setId(2L);
        assertThat(typeRessourceDTO1).isNotEqualTo(typeRessourceDTO2);
        typeRessourceDTO1.setId(null);
        assertThat(typeRessourceDTO1).isNotEqualTo(typeRessourceDTO2);
    }
}
