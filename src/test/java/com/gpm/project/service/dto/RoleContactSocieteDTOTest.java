package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleContactSocieteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleContactSocieteDTO.class);
        RoleContactSocieteDTO roleContactSocieteDTO1 = new RoleContactSocieteDTO();
        roleContactSocieteDTO1.setId(1L);
        RoleContactSocieteDTO roleContactSocieteDTO2 = new RoleContactSocieteDTO();
        assertThat(roleContactSocieteDTO1).isNotEqualTo(roleContactSocieteDTO2);
        roleContactSocieteDTO2.setId(roleContactSocieteDTO1.getId());
        assertThat(roleContactSocieteDTO1).isEqualTo(roleContactSocieteDTO2);
        roleContactSocieteDTO2.setId(2L);
        assertThat(roleContactSocieteDTO1).isNotEqualTo(roleContactSocieteDTO2);
        roleContactSocieteDTO1.setId(null);
        assertThat(roleContactSocieteDTO1).isNotEqualTo(roleContactSocieteDTO2);
    }
}
