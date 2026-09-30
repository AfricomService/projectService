package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AclEntryProjetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AclEntryProjet.class);
        AclEntryProjet aclEntryProjet1 = new AclEntryProjet();
        aclEntryProjet1.setId(1L);
        AclEntryProjet aclEntryProjet2 = new AclEntryProjet();
        aclEntryProjet2.setId(aclEntryProjet1.getId());
        assertThat(aclEntryProjet1).isEqualTo(aclEntryProjet2);
        aclEntryProjet2.setId(2L);
        assertThat(aclEntryProjet1).isNotEqualTo(aclEntryProjet2);
        aclEntryProjet1.setId(null);
        assertThat(aclEntryProjet1).isNotEqualTo(aclEntryProjet2);
    }
}
