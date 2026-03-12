package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriceJourFerieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatriceJourFerieDTO.class);
        MatriceJourFerieDTO matriceJourFerieDTO1 = new MatriceJourFerieDTO();
        matriceJourFerieDTO1.setId(1L);
        MatriceJourFerieDTO matriceJourFerieDTO2 = new MatriceJourFerieDTO();
        assertThat(matriceJourFerieDTO1).isNotEqualTo(matriceJourFerieDTO2);
        matriceJourFerieDTO2.setId(matriceJourFerieDTO1.getId());
        assertThat(matriceJourFerieDTO1).isEqualTo(matriceJourFerieDTO2);
        matriceJourFerieDTO2.setId(2L);
        assertThat(matriceJourFerieDTO1).isNotEqualTo(matriceJourFerieDTO2);
        matriceJourFerieDTO1.setId(null);
        assertThat(matriceJourFerieDTO1).isNotEqualTo(matriceJourFerieDTO2);
    }
}
