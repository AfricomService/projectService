package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriceFacturationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatriceFacturationDTO.class);
        MatriceFacturationDTO matriceFacturationDTO1 = new MatriceFacturationDTO();
        matriceFacturationDTO1.setId(1L);
        MatriceFacturationDTO matriceFacturationDTO2 = new MatriceFacturationDTO();
        assertThat(matriceFacturationDTO1).isNotEqualTo(matriceFacturationDTO2);
        matriceFacturationDTO2.setId(matriceFacturationDTO1.getId());
        assertThat(matriceFacturationDTO1).isEqualTo(matriceFacturationDTO2);
        matriceFacturationDTO2.setId(2L);
        assertThat(matriceFacturationDTO1).isNotEqualTo(matriceFacturationDTO2);
        matriceFacturationDTO1.setId(null);
        assertThat(matriceFacturationDTO1).isNotEqualTo(matriceFacturationDTO2);
    }
}
