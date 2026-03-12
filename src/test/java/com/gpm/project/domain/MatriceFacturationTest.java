package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriceFacturationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatriceFacturation.class);
        MatriceFacturation matriceFacturation1 = new MatriceFacturation();
        matriceFacturation1.setId(1L);
        MatriceFacturation matriceFacturation2 = new MatriceFacturation();
        matriceFacturation2.setId(matriceFacturation1.getId());
        assertThat(matriceFacturation1).isEqualTo(matriceFacturation2);
        matriceFacturation2.setId(2L);
        assertThat(matriceFacturation1).isNotEqualTo(matriceFacturation2);
        matriceFacturation1.setId(null);
        assertThat(matriceFacturation1).isNotEqualTo(matriceFacturation2);
    }
}
