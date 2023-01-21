package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.*;
import org.sql2o.Connection;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

public class TransactionRepository {
    private final Connection connection;
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
                "VALUES(:uuidStr, :eventUuidStr, :tradingAccountUuidStr, :securityUuidStr, " +
                ":quantity, :tradePrice, NOW())";

        try(var query = connection.createQuery(queryText)) {
            query.bind(tradingAccountTransaction);
            query.addParameter("uuidStr", tradingAccountTransaction.getUuid().toString());
            query.addParameter("eventUuidStr", tradingAccountTransaction.getEventUuid().toString());
            query.addParameter("tradingAccountUuidStr", tradingAccountTransaction.getTradingAccountUuid().toString());
            query.addParameter("securityUuidStr", tradingAccountTransaction.getTradingAccountUuid().toString());
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
    public List<SecurityHolding> list() {
        String queryText = "SELECT trading_account_transaction.trading_account_uuid as tradingAccountUuid, " +
                "security.uuid as securityUuid, security.name as securityName, security.symbol as securitySymbol, " +
                "security.type as securityType, security.LTP as LTP, " +
                "sum(quantity) as totalQuantity, " +
                "cast(sum(quantity * trade_price) / sum(quantity) as DECIMAL(10,2)) as avgPrice " +
                "FROM trading_account_transaction " +
                "JOIN `security` ON `security`.uuid = trading_account_transaction.security_uuid " +
                "GROUP BY trading_account_transaction.trading_account_uuid, security.uuid";

        try(var query = connection.createQuery(queryText)) {
            return query.executeAndFetch(SecurityHolding.class);
        }
    }

    public List<CurrentAccountTransaction> getCurrentAccountTransactionList(UUID eventUuid) {
        String queryText = "SELECT * from current_account_transaction WHERE event_uuid = :eventUuidStr";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("eventUuidStr", eventUuid.toString());
            query.addColumnMapping("event_uuid", "eventUuid");
            query.addColumnMapping("current_account_uuid", "currentAccountUuid");

            return query.executeAndFetch(CurrentAccountTransaction.class);
        }
    }

    public void remove(Transaction transaction) {
        String queryText = "DELETE FROM " + switch (transaction.getType()) {
            case CURRENT -> "current_account_transaction";
            case TRADING_SECURITY -> "trading_account_transaction";
            case TRADING_FUND -> "trading_account_fund_transaction";
        } + " WHERE uuid = :uuidStr";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", transaction.getUuid().toString());
            query.executeUpdate();
        }
    }
}
