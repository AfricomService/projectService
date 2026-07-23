package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactSocieteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactSociete.class);
        ContactSociete contactSociete1 = new ContactSociete();
        contactSociete1.setId(1L);
        ContactSociete contactSociete2 = new ContactSociete();
        contactSociete2.setId(contactSociete1.getId());
        assertThat(contactSociete1).isEqualTo(contactSociete2);
        contactSociete2.setId(2L);
        assertThat(contactSociete1).isNotEqualTo(contactSociete2);
        contactSociete1.setId(null);
        assertThat(contactSociete1).isNotEqualTo(contactSociete2);
    }
}
