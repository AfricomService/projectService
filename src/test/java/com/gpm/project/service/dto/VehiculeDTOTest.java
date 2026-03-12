package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehiculeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehiculeDTO.class);
        VehiculeDTO vehiculeDTO1 = new VehiculeDTO();
        vehiculeDTO1.setId(1L);
        VehiculeDTO vehiculeDTO2 = new VehiculeDTO();
        assertThat(vehiculeDTO1).isNotEqualTo(vehiculeDTO2);
        vehiculeDTO2.setId(vehiculeDTO1.getId());
        assertThat(vehiculeDTO1).isEqualTo(vehiculeDTO2);
        vehiculeDTO2.setId(2L);
        assertThat(vehiculeDTO1).isNotEqualTo(vehiculeDTO2);
        vehiculeDTO1.setId(null);
        assertThat(vehiculeDTO1).isNotEqualTo(vehiculeDTO2);
    }
}
