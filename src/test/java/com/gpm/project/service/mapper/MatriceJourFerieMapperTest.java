package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatriceJourFerieMapperTest {

    private MatriceJourFerieMapper matriceJourFerieMapper;

    @BeforeEach
    public void setUp() {
        matriceJourFerieMapper = new MatriceJourFerieMapperImpl();
    }
}
