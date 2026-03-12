package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffaireMapperTest {

    private AffaireMapper affaireMapper;

    @BeforeEach
    public void setUp() {
        affaireMapper = new AffaireMapperImpl();
    }
}
