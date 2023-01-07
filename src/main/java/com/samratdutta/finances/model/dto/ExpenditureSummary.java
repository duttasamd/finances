package com.samratdutta.finances.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ExpenditureSummary {
    private double amountSpent;
    private double budget;
    private double remainingFixed;
}
