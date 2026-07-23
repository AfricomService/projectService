package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AffaireSocieteAdjMapperTest {

    private AffaireSocieteAdjMapper affaireSocieteAdjMapper;

    @BeforeEach
    public void setUp() {
        affaireSocieteAdjMapper = new AffaireSocieteAdjMapperImpl();
    }
}
