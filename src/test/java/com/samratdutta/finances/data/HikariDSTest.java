package com.samratdutta.finances.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HikariDSTest{
    @Autowired
    private DataSource dataSource;
    @Test
    public void hikariConnectionPoolIsConfigured() {
        assertEquals("com.zaxxer.hikari.HikariDataSource", dataSource.getClass().getName());
    }
}