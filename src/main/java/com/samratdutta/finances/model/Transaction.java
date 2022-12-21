package com.samratdutta.finances.model;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private UUID uuid;
    private UUID eventUuid;
    private double amount;
    private Date timestamp;
}
