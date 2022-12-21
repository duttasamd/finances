package com.samratdutta.finances.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CashFlow {
    private UUID uuid;
    private UUID eventUuid;
    private UUID cashFlowCategoryUuid;
    private double amount;
    private Date timestamp;

}
