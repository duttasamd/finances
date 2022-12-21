package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.Account;
import com.samratdutta.finances.model.CurrentAccount;
import com.samratdutta.finances.model.FixedDepositAccount;
import com.samratdutta.finances.model.TradingAccount;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.security.InvalidParameterException;
import java.util.*;

@Slf4j
public class AccountRepository {
    Connection connection;
    private static final String CURRENT_ACCOUNT = "`current_account`";
    private static final String FIXED_DEPOSIT_ACCOUNT = "`fixed_deposit_account`";
    private static final String TRADING_ACCOUNT = "`trading_account`";

    public AccountRepository(Connection connection) {
        this.connection = connection;
    }

    public void addAccount(CurrentAccount account) {
        String queryText = "INSERT INTO " + CURRENT_ACCOUNT + "(uuid, name, number, currency, current_amount, " +
                " bank, bank_identifier) " +
                "VALUES(:uuidStr, :name, :number, :currency, :currentAmount, :bank, :bankIdentifier)";

        try(var query = connection.createQuery(queryText)) {
            query.bind(account);
            query.addParameter("uuidStr", account.getUuid().toString());
            query.executeUpdate();
        }
    }
    public void addAccount(FixedDepositAccount account) {
        String queryText = "INSERT INTO " + FIXED_DEPOSIT_ACCOUNT + "(uuid, name, number, currency, current_amount, " +
                "amount, bank, bank_identifier, rate_of_interest, date_of_deposit, date_of_maturity) " +
                "VALUES(:uuidStr, :name, :number, :currency, :currentAmount, :amount, :bank, :bankIdentifier, " +
                ":rateOfInterest, :dateOfDeposit, :dateOfMaturity)";

        try(var query = connection.createQuery(queryText)) {
            query.bind(account);
            query.addParameter("uuidStr", account.getUuid().toString());
            query.executeUpdate();
        }
    }
    public void addAccount(TradingAccount account) {
        String queryText = "INSERT INTO "+ TRADING_ACCOUNT +"(uuid, name, number, currency) " +
                "VALUES(:uuidStr, :name, :number, :currency)";

        try(var query = connection.createQuery(queryText)) {
            query.bind(account);
            query.addParameter("uuidStr", account.getUuid().toString());
            query.executeUpdate();
        }
    }

    public List<? extends Account> getAccounts(Account.Type type) {
        List<? extends Account> accounts;
        String accountType = switch (type) {
            case CURRENT -> CURRENT_ACCOUNT;
            case FIXED_DEPOSIT -> FIXED_DEPOSIT_ACCOUNT;
            case TRADING -> TRADING_ACCOUNT;
        };

        String queryText = "SELECT * FROM " + accountType;

        try(var query = connection.createQuery(queryText)) {
            switch (type) {
                case CURRENT -> {
                    CurrentAccount.addColumnMappings(query);
                    accounts = query.executeAndFetch(CurrentAccount.class);
                }
                case FIXED_DEPOSIT -> {
                    FixedDepositAccount.addColumnMappings(query);
                    accounts = query.executeAndFetch(FixedDepositAccount.class);
                }
                case TRADING -> {
                    TradingAccount.addColumnMappings(query);
                    accounts = query.executeAndFetch(TradingAccount.class);
                }
                default -> throw new InvalidParameterException();
            };
        }

        return accounts;
    }

    public Account getAccount(Account.Type type, UUID uuid) {
        Account account;

        String accountType = switch (type) {
            case CURRENT -> CURRENT_ACCOUNT;
            case FIXED_DEPOSIT -> FIXED_DEPOSIT_ACCOUNT;
            case TRADING -> TRADING_ACCOUNT;
        };

        String queryText = "SELECT * FROM " + accountType + " WHERE uuid = :uuidStr";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", uuid.toString());

            switch (type) {
                case CURRENT -> {
                    CurrentAccount.addColumnMappings(query);
                    account = query.executeAndFetchFirst(CurrentAccount.class);
                }
                case FIXED_DEPOSIT -> {
                    FixedDepositAccount.addColumnMappings(query);
                    account = query.executeAndFetchFirst(FixedDepositAccount.class);
                }
                case TRADING -> {
                    TradingAccount.addColumnMappings(query);
                    account = query.executeAndFetchFirst(TradingAccount.class);
                }
                default -> throw new InvalidParameterException();
            };

            return account;
        }
    }
}
