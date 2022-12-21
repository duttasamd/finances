package com.samratdutta.finances.model;

import java.util.UUID;

public class CurrentAccountTransactionType {
    enum Type {
        INCOME,
        EXPENDITURE
    }

    private UUID uuid;
    private String name;
    private Type type;

}
