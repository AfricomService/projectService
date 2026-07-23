package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactSocieteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactSocieteDTO.class);
        ContactSocieteDTO contactSocieteDTO1 = new ContactSocieteDTO();
        contactSocieteDTO1.setId(1L);
        ContactSocieteDTO contactSocieteDTO2 = new ContactSocieteDTO();
        assertThat(contactSocieteDTO1).isNotEqualTo(contactSocieteDTO2);
        contactSocieteDTO2.setId(contactSocieteDTO1.getId());
        assertThat(contactSocieteDTO1).isEqualTo(contactSocieteDTO2);
        contactSocieteDTO2.setId(2L);
        assertThat(contactSocieteDTO1).isNotEqualTo(contactSocieteDTO2);
        contactSocieteDTO1.setId(null);
        assertThat(contactSocieteDTO1).isNotEqualTo(contactSocieteDTO2);
    }
}
