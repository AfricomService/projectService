package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParentClassdefDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParentClassdefDTO.class);
        ParentClassdefDTO parentClassdefDTO1 = new ParentClassdefDTO();
        parentClassdefDTO1.setId(1L);
        ParentClassdefDTO parentClassdefDTO2 = new ParentClassdefDTO();
        assertThat(parentClassdefDTO1).isNotEqualTo(parentClassdefDTO2);
        parentClassdefDTO2.setId(parentClassdefDTO1.getId());
        assertThat(parentClassdefDTO1).isEqualTo(parentClassdefDTO2);
        parentClassdefDTO2.setId(2L);
        assertThat(parentClassdefDTO1).isNotEqualTo(parentClassdefDTO2);
        parentClassdefDTO1.setId(null);
        assertThat(parentClassdefDTO1).isNotEqualTo(parentClassdefDTO2);
    }
}
