package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehiculeMapperTest {

    private VehiculeMapper vehiculeMapper;

    @BeforeEach
    public void setUp() {
        vehiculeMapper = new VehiculeMapperImpl();
    }
}
