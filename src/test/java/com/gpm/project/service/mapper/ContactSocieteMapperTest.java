package com.gpm.project.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactSocieteMapperTest {

    private ContactSocieteMapper contactSocieteMapper;

    @BeforeEach
    public void setUp() {
        contactSocieteMapper = new ContactSocieteMapperImpl();
    }
}
