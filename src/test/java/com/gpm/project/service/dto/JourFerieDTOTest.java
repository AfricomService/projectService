package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JourFerieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JourFerieDTO.class);
        JourFerieDTO jourFerieDTO1 = new JourFerieDTO();
        jourFerieDTO1.setId(1L);
        JourFerieDTO jourFerieDTO2 = new JourFerieDTO();
        assertThat(jourFerieDTO1).isNotEqualTo(jourFerieDTO2);
        jourFerieDTO2.setId(jourFerieDTO1.getId());
        assertThat(jourFerieDTO1).isEqualTo(jourFerieDTO2);
        jourFerieDTO2.setId(2L);
        assertThat(jourFerieDTO1).isNotEqualTo(jourFerieDTO2);
        jourFerieDTO1.setId(null);
        assertThat(jourFerieDTO1).isNotEqualTo(jourFerieDTO2);
    }
}
