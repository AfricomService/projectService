package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeRessourceMapperTest {

    private TypeRessourceMapper typeRessourceMapper;

    @BeforeEach
    public void setUp() {
        typeRessourceMapper = new TypeRessourceMapperImpl();
    }
}
