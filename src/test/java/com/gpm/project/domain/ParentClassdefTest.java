package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParentClassdefTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParentClassdef.class);
        ParentClassdef parentClassdef1 = new ParentClassdef();
        parentClassdef1.setId(1L);
        ParentClassdef parentClassdef2 = new ParentClassdef();
        parentClassdef2.setId(parentClassdef1.getId());
        assertThat(parentClassdef1).isEqualTo(parentClassdef2);
        parentClassdef2.setId(2L);
        assertThat(parentClassdef1).isNotEqualTo(parentClassdef2);
        parentClassdef1.setId(null);
        assertThat(parentClassdef1).isNotEqualTo(parentClassdef2);
    }
}
