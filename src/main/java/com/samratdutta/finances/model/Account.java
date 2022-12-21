package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;
import org.sql2o.Query;

import java.security.InvalidParameterException;
import java.util.UUID;

@Data
public abstract class Account {

    public enum Type {
        CURRENT,
        FIXED_DEPOSIT,
        TRADING
    }

    protected UUID uuid;

    protected String name;
    protected String number;
    protected String currency;
    protected double currentAmount;
    public abstract Type getType();
}
