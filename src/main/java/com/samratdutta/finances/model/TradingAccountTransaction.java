package com.samratdutta.finances.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TradingAccountTransaction extends Transaction {
    public enum Type {
        BUY,
        SELL
    }
    private UUID securityUuid;
    private UUID tradingAccountUuid;
    private double quantity;
    private double tradePrice;
}
