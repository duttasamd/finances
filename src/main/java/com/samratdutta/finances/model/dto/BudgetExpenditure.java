package com.samratdutta.finances.model.dto;

import com.samratdutta.finances.model.Expenditure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetExpenditure {
    private Expenditure.Type type;
    private double budget;
    private double spent;
    List<ExpenditureDTO> expenditures;
}
