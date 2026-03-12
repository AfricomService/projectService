package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatriceJourFerieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatriceJourFerie.class);
        MatriceJourFerie matriceJourFerie1 = new MatriceJourFerie();
        matriceJourFerie1.setId(1L);
        MatriceJourFerie matriceJourFerie2 = new MatriceJourFerie();
        matriceJourFerie2.setId(matriceJourFerie1.getId());
        assertThat(matriceJourFerie1).isEqualTo(matriceJourFerie2);
        matriceJourFerie2.setId(2L);
        assertThat(matriceJourFerie1).isNotEqualTo(matriceJourFerie2);
        matriceJourFerie1.setId(null);
        assertThat(matriceJourFerie1).isNotEqualTo(matriceJourFerie2);
    }
}
