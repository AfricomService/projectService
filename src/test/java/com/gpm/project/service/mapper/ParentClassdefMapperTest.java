package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParentClassdefMapperTest {

    private ParentClassdefMapper parentClassdefMapper;

    @BeforeEach
    public void setUp() {
        parentClassdefMapper = new ParentClassdefMapperImpl();
    }
}
