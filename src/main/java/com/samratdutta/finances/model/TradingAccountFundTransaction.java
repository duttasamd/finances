package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
public class TradingAccountFundTransaction extends Transaction {
    private UUID tradingAccountUuid;
    private double amount;
    public Type getType() {
        return Type.TRADING_FUND;
    }
}
