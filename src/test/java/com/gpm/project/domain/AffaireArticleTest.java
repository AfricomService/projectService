package com.gpm.project.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gpm.project.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AffaireArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AffaireArticle.class);
        AffaireArticle affaireArticle1 = new AffaireArticle();
        affaireArticle1.setId(1L);
        AffaireArticle affaireArticle2 = new AffaireArticle();
        affaireArticle2.setId(affaireArticle1.getId());
        assertThat(affaireArticle1).isEqualTo(affaireArticle2);
        affaireArticle2.setId(2L);
        assertThat(affaireArticle1).isNotEqualTo(affaireArticle2);
        affaireArticle1.setId(null);
        assertThat(affaireArticle1).isNotEqualTo(affaireArticle2);
    }
}
