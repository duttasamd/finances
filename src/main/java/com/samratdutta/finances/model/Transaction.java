package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
public abstract class Transaction {
    public enum Type {
        CURRENT,
        TRADING_FUND,
        TRADING_SECURITY
    }
    private UUID uuid;
    private UUID eventUuid;
    private Date timestamp;
    public abstract Type getType();
}
