package com.gpm.project.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffaireArticleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffaireArticleDTO.class);
        AffaireArticleDTO affaireArticleDTO1 = new AffaireArticleDTO();
        affaireArticleDTO1.setId(1L);
        AffaireArticleDTO affaireArticleDTO2 = new AffaireArticleDTO();
        assertThat(affaireArticleDTO1).isNotEqualTo(affaireArticleDTO2);
        affaireArticleDTO2.setId(affaireArticleDTO1.getId());
        assertThat(affaireArticleDTO1).isEqualTo(affaireArticleDTO2);
        affaireArticleDTO2.setId(2L);
        assertThat(affaireArticleDTO1).isNotEqualTo(affaireArticleDTO2);
        affaireArticleDTO1.setId(null);
        assertThat(affaireArticleDTO1).isNotEqualTo(affaireArticleDTO2);
    }
}
