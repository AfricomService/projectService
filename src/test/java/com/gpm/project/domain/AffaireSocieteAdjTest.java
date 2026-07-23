package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffaireSocieteAdjTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffaireSocieteAdj.class);
        AffaireSocieteAdj affaireSocieteAdj1 = new AffaireSocieteAdj();
        affaireSocieteAdj1.setId(1L);
        AffaireSocieteAdj affaireSocieteAdj2 = new AffaireSocieteAdj();
        affaireSocieteAdj2.setId(affaireSocieteAdj1.getId());
        assertThat(affaireSocieteAdj1).isEqualTo(affaireSocieteAdj2);
        affaireSocieteAdj2.setId(2L);
        assertThat(affaireSocieteAdj1).isNotEqualTo(affaireSocieteAdj2);
        affaireSocieteAdj1.setId(null);
        assertThat(affaireSocieteAdj1).isNotEqualTo(affaireSocieteAdj2);
    }
}
