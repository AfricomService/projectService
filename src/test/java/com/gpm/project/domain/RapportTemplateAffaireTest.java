package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RapportTemplateAffaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RapportTemplateAffaire.class);
        RapportTemplateAffaire rapportTemplateAffaire1 = new RapportTemplateAffaire();
        rapportTemplateAffaire1.setId(1L);
        RapportTemplateAffaire rapportTemplateAffaire2 = new RapportTemplateAffaire();
        rapportTemplateAffaire2.setId(rapportTemplateAffaire1.getId());
        assertThat(rapportTemplateAffaire1).isEqualTo(rapportTemplateAffaire2);
        rapportTemplateAffaire2.setId(2L);
        assertThat(rapportTemplateAffaire1).isNotEqualTo(rapportTemplateAffaire2);
        rapportTemplateAffaire1.setId(null);
        assertThat(rapportTemplateAffaire1).isNotEqualTo(rapportTemplateAffaire2);
    }
}
