package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAuthSocieteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAuthSociete.class);
        UserAuthSociete userAuthSociete1 = new UserAuthSociete();
        userAuthSociete1.setId(1L);
        UserAuthSociete userAuthSociete2 = new UserAuthSociete();
        userAuthSociete2.setId(userAuthSociete1.getId());
        assertThat(userAuthSociete1).isEqualTo(userAuthSociete2);
        userAuthSociete2.setId(2L);
        assertThat(userAuthSociete1).isNotEqualTo(userAuthSociete2);
        userAuthSociete1.setId(null);
        assertThat(userAuthSociete1).isNotEqualTo(userAuthSociete2);
    }
}
