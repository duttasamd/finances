package com.samratdutta.finances.service;

import com.samratdutta.finances.helper.exception.NotEnoughBalanceException;
import com.samratdutta.finances.helper.exception.NotFoundException;
import com.samratdutta.finances.model.*;
import com.samratdutta.finances.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sql2o.Sql2o;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// MANAGES : trading_account_transaction and security tables
@Service
public class SecurityHoldingService {
    @Autowired
    private DataSource dataSource;

    public UUID addSecurity(Security security) {
        Stock stock;
        try {
            stock = YahooFinance.get(security.getSymbol());
        } catch (IOException e) {
            throw new InvalidParameterException("Symbol not found");
        }

        security.setLTP(stock.getQuote().getPrice().doubleValue());
        security.setPreviousClose(stock.getQuote().getPreviousClose().doubleValue());
        security.setChange(stock.getQuote().getChange().doubleValue());
        security.setChangePercent(stock.getQuote().getChangeInPercent().doubleValue());
        security.setExchange(stock.getStockExchange());
        security.setName(stock.getName());

        UUID uuid = UUID.randomUUID();
        security.setUuid(uuid);

        Sql2o financesDb = new Sql2o(dataSource);
        try(var connection = financesDb.beginTransaction()){
            var securityRepository = new SecurityRepository(connection);
            securityRepository.add(security);
            connection.commit();
            return uuid;
        }
    }

    public UUID buySecurity(TradingAccountTransaction tradingAccountTransaction)
            throws NotFoundException, NotEnoughBalanceException {
        UUID eventUUID = UUID.randomUUID();

        var event = Event.builder()
                .type(Event.Type.SECURITY_BUY)
                .uuid(eventUUID)
                .build();

        tradingAccountTransaction.setUuid(UUID.randomUUID());
        tradingAccountTransaction.setEventUuid(event.getUuid());

        Sql2o financesDb = new Sql2o(dataSource);
        try(var connection = financesDb.beginTransaction()){
            var eventRepository = new EventRepository(connection);
            var accountRepository = new AccountRepository(connection);
            var tradingAccountTransactionRepository = new TransactionRepository(connection);

            TradingAccount tradingAccount = (TradingAccount) accountRepository.getAccount(Account.Type.TRADING,
                    tradingAccountTransaction.getTradingAccountUuid());

            if(tradingAccount == null)
                throw new NotFoundException("Trading Account", tradingAccountTransaction.getUuid());

            double transactionValue = tradingAccountTransaction.getTradePrice() *
                    tradingAccountTransaction.getQuantity();

            if(tradingAccount.getCurrentAmount() < transactionValue) {
                throw new NotEnoughBalanceException("Trading Account", tradingAccount.getName(), transactionValue);
            }

            eventRepository.add(event);
            accountRepository.adjustCurrentAmount(tradingAccount, transactionValue, false);
            tradingAccountTransactionRepository.add(tradingAccountTransaction);
            connection.commit();

            return eventUUID;
        }
    }

    public Map<UUID, List<SecurityHolding>> getSecurityHoldingMap() {
        Map<UUID, List<SecurityHolding>> securityHoldingMap = new HashMap<>();
        List<SecurityHolding> securityHoldings;

        Sql2o financesDb = new Sql2o(dataSource);
        try(var connection = financesDb.open()){
            var transactionRepository = new TransactionRepository(connection);
            securityHoldings = transactionRepository.list();
        }

        return securityHoldings.stream().collect(Collectors.groupingBy(x -> x.getSecurityUuid()));
    }

    public double getQuote(String symbol) {
        Stock stock;
        try {
            stock = YahooFinance.get(symbol);
        } catch (IOException e) {
            throw new InvalidParameterException("Symbol not found");
        }

        return stock.getQuote().getPrice().doubleValue();
    }
}
