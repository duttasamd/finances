package com.samratdutta.finances.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ExpenditureSummary {
    private double fixedSpent;
    private double variableSpent;
    private double budget;
    private double remainingFixed;
}
