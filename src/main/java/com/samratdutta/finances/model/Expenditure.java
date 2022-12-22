package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Expenditure {
    public enum Type {
        RESTAURANT,
        GROCERIES,
        INSURANCE,
        TRANSPORT,
        UTILITY,
        PURCHASE,
        MISCELLANEOUS
        
    }
    private UUID uuid;
    private UUID eventUuid;
    private Type type;
    private double amount;
    private String comment;
    private Date timestamp;
    private String currency;
}
