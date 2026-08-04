package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleContactSocieteMapperTest {

    private RoleContactSocieteMapper roleContactSocieteMapper;

    @BeforeEach
    public void setUp() {
        roleContactSocieteMapper = new RoleContactSocieteMapperImpl();
    }
}
