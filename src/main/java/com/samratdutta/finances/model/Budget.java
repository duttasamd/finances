package com.samratdutta.finances.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class Budget {
    public enum Type {
        MONTHLY,
        SPECIAL
    }
    private UUID uuid;
    private String name;
    private Type type;
    private Timestamp start;
    private Timestamp createdAt;
    private Timestamp end;
}
