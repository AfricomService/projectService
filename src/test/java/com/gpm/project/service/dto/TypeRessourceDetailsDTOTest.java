package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeRessourceDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRessourceDetailsDTO.class);
        TypeRessourceDetailsDTO typeRessourceDetailsDTO1 = new TypeRessourceDetailsDTO();
        typeRessourceDetailsDTO1.setId(1L);
        TypeRessourceDetailsDTO typeRessourceDetailsDTO2 = new TypeRessourceDetailsDTO();
        assertThat(typeRessourceDetailsDTO1).isNotEqualTo(typeRessourceDetailsDTO2);
        typeRessourceDetailsDTO2.setId(typeRessourceDetailsDTO1.getId());
        assertThat(typeRessourceDetailsDTO1).isEqualTo(typeRessourceDetailsDTO2);
        typeRessourceDetailsDTO2.setId(2L);
        assertThat(typeRessourceDetailsDTO1).isNotEqualTo(typeRessourceDetailsDTO2);
        typeRessourceDetailsDTO1.setId(null);
        assertThat(typeRessourceDetailsDTO1).isNotEqualTo(typeRessourceDetailsDTO2);
    }
}
