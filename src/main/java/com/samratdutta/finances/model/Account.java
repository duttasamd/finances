package com.samratdutta.finances.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.sql2o.Query;

import java.security.InvalidParameterException;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
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
