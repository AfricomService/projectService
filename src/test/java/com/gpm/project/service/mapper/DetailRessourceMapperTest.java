package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DetailRessourceMapperTest {

    private DetailRessourceMapper detailRessourceMapper;

    @BeforeEach
    public void setUp() {
        detailRessourceMapper = new DetailRessourceMapperImpl();
    }
}
