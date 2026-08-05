package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAuthSocieteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAuthSocieteDTO.class);
        UserAuthSocieteDTO userAuthSocieteDTO1 = new UserAuthSocieteDTO();
        userAuthSocieteDTO1.setId(1L);
        UserAuthSocieteDTO userAuthSocieteDTO2 = new UserAuthSocieteDTO();
        assertThat(userAuthSocieteDTO1).isNotEqualTo(userAuthSocieteDTO2);
        userAuthSocieteDTO2.setId(userAuthSocieteDTO1.getId());
        assertThat(userAuthSocieteDTO1).isEqualTo(userAuthSocieteDTO2);
        userAuthSocieteDTO2.setId(2L);
        assertThat(userAuthSocieteDTO1).isNotEqualTo(userAuthSocieteDTO2);
        userAuthSocieteDTO1.setId(null);
        assertThat(userAuthSocieteDTO1).isNotEqualTo(userAuthSocieteDTO2);
    }
}
