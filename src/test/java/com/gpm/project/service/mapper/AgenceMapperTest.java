package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgenceMapperTest {

    private AgenceMapper agenceMapper;

    @BeforeEach
    public void setUp() {
        agenceMapper = new AgenceMapperImpl();
    }
}
