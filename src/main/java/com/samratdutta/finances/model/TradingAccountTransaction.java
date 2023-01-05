package com.samratdutta.finances.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
public class TradingAccountTransaction extends Transaction {
    public enum Type {
        BUY,
        SELL
    }
    private UUID securityUuid;
    private UUID tradingAccountUuid;
    private double quantity;
    private double tradePrice;
    public Transaction.Type getType() {
        return Transaction.Type.TRADING_SECURITY;
    }
}
