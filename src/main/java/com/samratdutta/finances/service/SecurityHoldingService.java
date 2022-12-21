package com.samratdutta.finances.service;

import com.samratdutta.finances.model.Security;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

// MANAGES : trading_account_transaction and security tables
public class SecurityHoldingService {
    @Autowired
    private DataSource dataSource;
    public void add(Security security) {
        
    }
}
