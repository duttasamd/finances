package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SecurityHolding {
    private UUID securityUuid;
    private String securityName;
    private String securitySymbol;
    private Security.Type securityType;
    private UUID tradingAccountUuid;
    private double LTP;
    private double buyAvgPrice;
    private double sellAvgPrice;
    private double buyTotalQuantity;
    private double sellTotalQuantity;
    private double totalQuantity;
    private double avgPrice;

    public double getQuantity() {
        return buyTotalQuantity - sellTotalQuantity;
    }

    public double getCurrentValuation() {
        return LTP * getQuantity();
    }

    public double getProfit() {
        return getCurrentValuation() - (sellAvgPrice * sellTotalQuantity) - (buyAvgPrice * buyTotalQuantity);
    }
}
