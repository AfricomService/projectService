package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffaireSocieteAdjDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffaireSocieteAdjDTO.class);
        AffaireSocieteAdjDTO affaireSocieteAdjDTO1 = new AffaireSocieteAdjDTO();
        affaireSocieteAdjDTO1.setId(1L);
        AffaireSocieteAdjDTO affaireSocieteAdjDTO2 = new AffaireSocieteAdjDTO();
        assertThat(affaireSocieteAdjDTO1).isNotEqualTo(affaireSocieteAdjDTO2);
        affaireSocieteAdjDTO2.setId(affaireSocieteAdjDTO1.getId());
        assertThat(affaireSocieteAdjDTO1).isEqualTo(affaireSocieteAdjDTO2);
        affaireSocieteAdjDTO2.setId(2L);
        assertThat(affaireSocieteAdjDTO1).isNotEqualTo(affaireSocieteAdjDTO2);
        affaireSocieteAdjDTO1.setId(null);
        assertThat(affaireSocieteAdjDTO1).isNotEqualTo(affaireSocieteAdjDTO2);
    }
}
