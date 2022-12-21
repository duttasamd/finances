package com.samratdutta.finances.model;

import java.util.Date;

public class Event {
    enum Type {
        ADJUSTMENT_ADD_BALANCE,
        ADJUSTMENT_REMOVE_BALANCE,
        EXPENDITURE,
        INCOME,
        REALLOCATION,
        SECURITY_BUY,
        SECURITY_SELL,
        REMITTANCE
    }
    private int id;
    private Type type;
    private Date timestamp;

}
