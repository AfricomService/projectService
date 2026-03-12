package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatriceFacturationMapperTest {

    private MatriceFacturationMapper matriceFacturationMapper;

    @BeforeEach
    public void setUp() {
        matriceFacturationMapper = new MatriceFacturationMapperImpl();
    }
}
