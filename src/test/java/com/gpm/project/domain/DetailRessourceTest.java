package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailRessourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailRessource.class);
        DetailRessource detailRessource1 = new DetailRessource();
        detailRessource1.setId(1L);
        DetailRessource detailRessource2 = new DetailRessource();
        detailRessource2.setId(detailRessource1.getId());
        assertThat(detailRessource1).isEqualTo(detailRessource2);
        detailRessource2.setId(2L);
        assertThat(detailRessource1).isNotEqualTo(detailRessource2);
        detailRessource1.setId(null);
        assertThat(detailRessource1).isNotEqualTo(detailRessource2);
    }
}
