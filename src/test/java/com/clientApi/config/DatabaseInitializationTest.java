package com.clientApi.config;


import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
@SpringBootTest
@ActiveProfiles("test")
*/
public class DatabaseInitializationTest {

    /*
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testSequenceInitialization() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('client_sequence')", Long.class);
        assertEquals(1L, nextVal);
    }*/
}