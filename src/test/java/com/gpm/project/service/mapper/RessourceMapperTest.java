package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RessourceMapperTest {

    private RessourceMapper ressourceMapper;

    @BeforeEach
    public void setUp() {
        ressourceMapper = new RessourceMapperImpl();
    }
}
