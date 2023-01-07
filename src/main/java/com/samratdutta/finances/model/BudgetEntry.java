package com.samratdutta.finances.model;

import lombok.Data;

import java.util.UUID;

@Data
public class BudgetEntry {
    private UUID uuid;
    private UUID budgetUuid;
    private Expenditure.Type type;
    private double amount;
}
