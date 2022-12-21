package com.samratdutta.finances.model;

import lombok.Builder;
import lombok.Data;
import org.sql2o.Query;

@Data
public class CurrentAccount extends Account {
    private static final String TABLE_NAME = "current_account";
    private boolean isPrimary;
    private String bank;
    private String bankIdentifier;

    public static void addColumnMappings(Query query) {
        query.addColumnMapping("is_primary", "isPrimary");
        query.addColumnMapping("bank_identifier", "bankIdentifier");
        query.addColumnMapping("current_amount", "currentAmount");
    }

    public Account.Type getType() {
        return Type.CURRENT;
    }
}
