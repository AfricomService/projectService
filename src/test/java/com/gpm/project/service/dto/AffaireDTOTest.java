package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffaireDTO.class);
        AffaireDTO affaireDTO1 = new AffaireDTO();
        affaireDTO1.setId(1L);
        AffaireDTO affaireDTO2 = new AffaireDTO();
        assertThat(affaireDTO1).isNotEqualTo(affaireDTO2);
        affaireDTO2.setId(affaireDTO1.getId());
        assertThat(affaireDTO1).isEqualTo(affaireDTO2);
        affaireDTO2.setId(2L);
        assertThat(affaireDTO1).isNotEqualTo(affaireDTO2);
        affaireDTO1.setId(null);
        assertThat(affaireDTO1).isNotEqualTo(affaireDTO2);
    }
}
