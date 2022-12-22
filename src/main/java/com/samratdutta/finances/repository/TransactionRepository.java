package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.CurrentAccountTransaction;
import com.samratdutta.finances.model.TradingAccountFundTransaction;
import com.samratdutta.finances.model.TradingAccountTransaction;
import com.samratdutta.finances.model.Transaction;
import org.sql2o.Connection;

public class TransactionRepository {
    private Connection connection;
    public TransactionRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Transaction transaction) {
        switch (transaction.getType()) {
            case CURRENT -> add((CurrentAccountTransaction) transaction);
            case TRADING_SECURITY -> add((TradingAccountTransaction) transaction);
            case TRADING_FUND -> add((TradingAccountFundTransaction) transaction);
        }
    }
    public void add(TradingAccountTransaction tradingAccountTransaction) {
        String queryText = "INSERT INTO trading_account_transaction(uuid, event_uuid, trading_account_uuid, security_uuid, " +
                "quantity, trade_price, timestamp) " +
                "VALUES(:uuidStr, :eventUuidStr, :tradingAccountUuidStr, :securityUuid, " +
                ":quantity, :tradePrice, NOW())";

        try(var query = connection.createQuery(queryText)) {
            query.bind(tradingAccountTransaction);
            query.addParameter("uuidStr", tradingAccountTransaction.getUuid().toString());
            query.addParameter("eventUuidStr", tradingAccountTransaction.getEventUuid().toString());
            query.addParameter("tradingAccountUuidStr", tradingAccountTransaction.getTradingAccountUuid().toString());
            query.executeUpdate();
        }
    }

    public void add(CurrentAccountTransaction currentAccountTransaction) {
        String queryText = "INSERT INTO current_account_transaction(uuid, event_uuid, current_account_uuid, " +
                "amount, timestamp) " +
                "VALUES(:uuidStr, :eventUuidStr, :currentAccountUuidStr, :amount, NOW())";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", currentAccountTransaction.getUuid().toString());
            query.addParameter("eventUuidStr", currentAccountTransaction.getEventUuid().toString());
            query.addParameter("currentAccountUuidStr", currentAccountTransaction.getCurrentAccountUuid().toString());
            query.addParameter("amount", currentAccountTransaction.getAmount());
            query.executeUpdate();
        }
    }

    public void add(TradingAccountFundTransaction tradingAccountFundTransaction) {
        String queryText = "INSERT INTO trading_account_fund_transaction(uuid, event_uuid, trading_account_uuid, " +
                "amount, timestamp) " +
                "VALUES(:uuidStr, :eventUuidStr, :tradingAccountUuidStr, :amount, NOW())";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", tradingAccountFundTransaction.getUuid().toString());
            query.addParameter("eventUuidStr", tradingAccountFundTransaction.getEventUuid().toString());
            query.addParameter("tradingAccountUuidStr", tradingAccountFundTransaction.getTradingAccountUuid().toString());
            query.addParameter("amount", tradingAccountFundTransaction.getAmount());
            query.executeUpdate();
        }
    }
}
