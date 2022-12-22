package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Event {
    public enum Type {
        ADJUSTMENT_ADD_BALANCE,
        ADJUSTMENT_REMOVE_BALANCE,
        EXPENDITURE,
        INCOME,
        REALLOCATION,
        SECURITY_BUY,
        SECURITY_SELL,
        REMITTANCE
    }
    private UUID uuid;
    private Type type;
    private Date timestamp;
}
