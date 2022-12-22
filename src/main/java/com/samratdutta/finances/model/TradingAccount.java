package com.samratdutta.finances.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.sql2o.Query;

import java.util.*;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@AllArgsConstructor
public class TradingAccount extends Account {
    public static void addColumnMappings(Query query) {
        query.addColumnMapping("current_amount", "currentAmount");
    }

    @Override
    public Account.Type getType() {
        return Type.TRADING;
    }
    private List<TradingAccountTransaction> transactionList;
    public Set<SecurityHolding> getHoldings() {
        if(transactionList == null)
            return null;

        Set<SecurityHolding> holdings = new HashSet<>();

        Map<UUID, Double> sellQuantityMap
            = transactionList.stream()
                .filter(x -> x.getQuantity() < 0)
                .collect(
                        Collectors.groupingBy(TradingAccountTransaction::getSecurityUuid,
                                Collectors.summingDouble(TradingAccountTransaction::getQuantity))
                );

        Map<UUID, Double> sellAvgPriceMap
                = transactionList.stream()
                .filter(x -> x.getQuantity() < 0)
                .collect(
                        Collectors.groupingBy(TradingAccountTransaction::getSecurityUuid,
                                Collectors.averagingDouble(x -> x.getTradePrice() * x.getQuantity()))
                );

        Map<UUID, Double> buyQuantityMap
                = transactionList.stream()
                .filter(x -> x.getQuantity() > 0)
                .collect(
                        Collectors.groupingBy(TradingAccountTransaction::getSecurityUuid,
                                Collectors.summingDouble(TradingAccountTransaction::getQuantity))
                );

        Map<UUID, Double> buyAvgPriceMap
                = transactionList.stream()
                .filter(x -> x.getQuantity() < 0)
                .collect(
                        Collectors.groupingBy(TradingAccountTransaction::getSecurityUuid,
                                Collectors.averagingDouble(x -> x.getTradePrice() * x.getQuantity()))
                );

        Set<UUID> securities = transactionList.stream().map(TradingAccountTransaction::getSecurityUuid).collect(Collectors.toSet());

        for (UUID securityUuid : securities) {
            SecurityHolding holding
                    = SecurityHolding.builder()
                    .securityUuid(securityUuid)
                    .buyAvgPrice(Optional.ofNullable(buyAvgPriceMap.get(securityUuid)).orElse(0.0))
                    .buyTotalQuantity(Optional.ofNullable(buyQuantityMap.get(securityUuid)).orElse(0.0))
                    .sellAvgPrice(Optional.ofNullable(sellAvgPriceMap.get(securityUuid)).orElse(0.0))
                    .sellTotalQuantity(Optional.ofNullable(sellQuantityMap.get(securityUuid)).orElse(0.0))
                    .build();

            holdings.add(holding);
        }

        return holdings;
    }

    public double getTotalAmount() {
        double holdingBalance = getHoldings() == null ? 0 : getHoldings().stream().mapToDouble(SecurityHolding::getCurrentValuation).sum();
        return holdingBalance + currentAmount;
    }
}
