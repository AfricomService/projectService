package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAuthSocieteMapperTest {

    private UserAuthSocieteMapper userAuthSocieteMapper;

    @BeforeEach
    public void setUp() {
        userAuthSocieteMapper = new UserAuthSocieteMapperImpl();
    }
}
