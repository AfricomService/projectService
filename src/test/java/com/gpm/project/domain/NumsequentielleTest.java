package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NumsequentielleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Numsequentielle.class);
        Numsequentielle numsequentielle1 = new Numsequentielle();
        numsequentielle1.setId(1L);
        Numsequentielle numsequentielle2 = new Numsequentielle();
        numsequentielle2.setId(numsequentielle1.getId());
        assertThat(numsequentielle1).isEqualTo(numsequentielle2);
        numsequentielle2.setId(2L);
        assertThat(numsequentielle1).isNotEqualTo(numsequentielle2);
        numsequentielle1.setId(null);
        assertThat(numsequentielle1).isNotEqualTo(numsequentielle2);
    }
}
