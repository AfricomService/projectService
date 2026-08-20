package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailRessourceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailRessourceDTO.class);
        DetailRessourceDTO detailRessourceDTO1 = new DetailRessourceDTO();
        detailRessourceDTO1.setId(1L);
        DetailRessourceDTO detailRessourceDTO2 = new DetailRessourceDTO();
        assertThat(detailRessourceDTO1).isNotEqualTo(detailRessourceDTO2);
        detailRessourceDTO2.setId(detailRessourceDTO1.getId());
        assertThat(detailRessourceDTO1).isEqualTo(detailRessourceDTO2);
        detailRessourceDTO2.setId(2L);
        assertThat(detailRessourceDTO1).isNotEqualTo(detailRessourceDTO2);
        detailRessourceDTO1.setId(null);
        assertThat(detailRessourceDTO1).isNotEqualTo(detailRessourceDTO2);
    }
}
