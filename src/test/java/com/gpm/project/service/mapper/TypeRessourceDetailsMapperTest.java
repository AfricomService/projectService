package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeRessourceDetailsMapperTest {

    private TypeRessourceDetailsMapper typeRessourceDetailsMapper;

    @BeforeEach
    public void setUp() {
        typeRessourceDetailsMapper = new TypeRessourceDetailsMapperImpl();
    }
}
