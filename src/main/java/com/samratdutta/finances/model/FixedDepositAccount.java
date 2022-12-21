package com.samratdutta.finances.model;

import lombok.Data;
import org.sql2o.Query;

import java.util.Date;

@Data
public class FixedDepositAccount extends Account {
    private double amount;
    private double rateOfInterest;
    private Date dateOfDeposit;
    private Date dateOfMaturity;
    private String bank;
    private String bankIdentifier;

    public static void addColumnMappings(Query query) {
        query.addColumnMapping("rate_of_interest", "rateOfInterest");
        query.addColumnMapping("date_of_deposit", "dateOfDeposit");
        query.addColumnMapping("date_of_maturity", "dateOfMaturity");
        query.addColumnMapping("bank_identifier", "bankIdentifier");
        query.addColumnMapping("current_amount", "currentAmount");
    }

    public Account.Type getType() {
        return Type.FIXED_DEPOSIT;
    }
}
