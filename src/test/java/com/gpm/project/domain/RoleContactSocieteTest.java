package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleContactSocieteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleContactSociete.class);
        RoleContactSociete roleContactSociete1 = new RoleContactSociete();
        roleContactSociete1.setId(1L);
        RoleContactSociete roleContactSociete2 = new RoleContactSociete();
        roleContactSociete2.setId(roleContactSociete1.getId());
        assertThat(roleContactSociete1).isEqualTo(roleContactSociete2);
        roleContactSociete2.setId(2L);
        assertThat(roleContactSociete1).isNotEqualTo(roleContactSociete2);
        roleContactSociete1.setId(null);
        assertThat(roleContactSociete1).isNotEqualTo(roleContactSociete2);
    }
}
