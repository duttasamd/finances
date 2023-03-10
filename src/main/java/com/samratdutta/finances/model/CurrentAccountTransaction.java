package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
public class CurrentAccountTransaction extends Transaction {
    private UUID currentAccountUuid;
    private double amount;
    public Type getType() {
        return Type.CURRENT;
    }
}
