package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JourFerieMapperTest {

    private JourFerieMapper jourFerieMapper;

    @BeforeEach
    public void setUp() {
        jourFerieMapper = new JourFerieMapperImpl();
    }
}
