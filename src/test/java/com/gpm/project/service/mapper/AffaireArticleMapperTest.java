package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffaireArticleMapperTest {

    private AffaireArticleMapper affaireArticleMapper;

    @BeforeEach
    public void setUp() {
        affaireArticleMapper = new AffaireArticleMapperImpl();
    }
}
