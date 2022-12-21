package com.samratdutta.finances.model;

import java.util.UUID;

public class CashFlowCategory {
    public enum Type {
        INCOME,
        EXPENDITURE
    }
    private UUID uuid;
    private Type type;
    private String name;
}
