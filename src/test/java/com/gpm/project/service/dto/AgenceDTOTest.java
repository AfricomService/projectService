package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgenceDTO.class);
        AgenceDTO agenceDTO1 = new AgenceDTO();
        agenceDTO1.setId(1L);
        AgenceDTO agenceDTO2 = new AgenceDTO();
        assertThat(agenceDTO1).isNotEqualTo(agenceDTO2);
        agenceDTO2.setId(agenceDTO1.getId());
        assertThat(agenceDTO1).isEqualTo(agenceDTO2);
        agenceDTO2.setId(2L);
        assertThat(agenceDTO1).isNotEqualTo(agenceDTO2);
        agenceDTO1.setId(null);
        assertThat(agenceDTO1).isNotEqualTo(agenceDTO2);
    }
}
