package com.samratdutta.finances.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.sql2o.Query;

import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
public class FixedDepositAccount extends Account {
    private double amount;
    private double rateOfInterest;
    private Date dateOfDeposit;
    private Date dateOfMaturity;
    private String bank;
    private String bankIdentifier;
    private boolean isClosed;

    public static void addColumnMappings(Query query) {
        query.addColumnMapping("rate_of_interest", "rateOfInterest");
        query.addColumnMapping("date_of_deposit", "dateOfDeposit");
        query.addColumnMapping("date_of_maturity", "dateOfMaturity");
        query.addColumnMapping("bank_identifier", "bankIdentifier");
        query.addColumnMapping("current_amount", "currentAmount");
        query.addColumnMapping("is_closed", "isClosed");
    }

    public Account.Type getType() {
        return Type.FIXED_DEPOSIT;
    }
}
