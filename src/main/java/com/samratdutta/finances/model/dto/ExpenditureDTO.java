package com.samratdutta.finances.model.dto;

import com.samratdutta.finances.model.Expenditure;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ExpenditureDTO {
    private UUID uuid;
    private UUID eventUuid;
    private Expenditure.Type type;
    private double amount;
    private String comment;
    private Date timestamp;
    private String currency;
    private String account;

}
