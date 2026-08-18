package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeRessourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRessource.class);
        TypeRessource typeRessource1 = new TypeRessource();
        typeRessource1.setId(1L);
        TypeRessource typeRessource2 = new TypeRessource();
        typeRessource2.setId(typeRessource1.getId());
        assertThat(typeRessource1).isEqualTo(typeRessource2);
        typeRessource2.setId(2L);
        assertThat(typeRessource1).isNotEqualTo(typeRessource2);
        typeRessource1.setId(null);
        assertThat(typeRessource1).isNotEqualTo(typeRessource2);
    }
}
