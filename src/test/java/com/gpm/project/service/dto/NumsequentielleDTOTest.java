package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NumsequentielleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NumsequentielleDTO.class);
        NumsequentielleDTO numsequentielleDTO1 = new NumsequentielleDTO();
        numsequentielleDTO1.setId(1L);
        NumsequentielleDTO numsequentielleDTO2 = new NumsequentielleDTO();
        assertThat(numsequentielleDTO1).isNotEqualTo(numsequentielleDTO2);
        numsequentielleDTO2.setId(numsequentielleDTO1.getId());
        assertThat(numsequentielleDTO1).isEqualTo(numsequentielleDTO2);
        numsequentielleDTO2.setId(2L);
        assertThat(numsequentielleDTO1).isNotEqualTo(numsequentielleDTO2);
        numsequentielleDTO1.setId(null);
        assertThat(numsequentielleDTO1).isNotEqualTo(numsequentielleDTO2);
    }
}
