package com.samratdutta.finances.service;

import com.samratdutta.finances.model.*;
import com.samratdutta.finances.repository.AccountRepository;
import com.samratdutta.finances.repository.EventRepository;
import com.samratdutta.finances.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.security.InvalidParameterException;
import java.util.*;

@Slf4j
@Service
public class AccountService {
    @Autowired
    private DataSource dataSource;
    public UUID addAccount(Account account) {
        UUID uuid = UUID.randomUUID();
        LOGGER.debug("{}", uuid);
        account.setUuid(uuid);

        //TODO Make sure account with same name does not exist for user.
        Sql2o financesDb = new Sql2o(dataSource);
        try(var connection = financesDb.open()){
            AccountRepository accountRepository = new AccountRepository(connection);
            switch (account.getType()) {
                case CURRENT -> accountRepository.addAccount((CurrentAccount) account);
                case FIXED_DEPOSIT -> accountRepository.addAccount((FixedDepositAccount) account);
                case TRADING -> accountRepository.addAccount((TradingAccount) account);
                default -> throw new InvalidParameterException();
            }
            connection.commit();
            return uuid;
        }
    }

    public List<? extends Account> getAccounts(Account.Type type) {
        List<? extends Account> accounts;
        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.open()) {
            var accountRepository = new AccountRepository(connection);
            return accountRepository.getAccounts(type);
        }
    }

    public Map<Account.Type, List<? extends Account>> getAccounts() {
        Map<Account.Type, List<? extends Account>> accountMap = new HashMap<>();

        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.open()) {
            var accountRepository = new AccountRepository(connection);

            accountMap.put(Account.Type.CURRENT, accountRepository.getAccounts(Account.Type.CURRENT));
            accountMap.put(Account.Type.FIXED_DEPOSIT, accountRepository.getAccounts(Account.Type.FIXED_DEPOSIT));
            accountMap.put(Account.Type.TRADING, accountRepository.getAccounts(Account.Type.TRADING));

            return accountMap;
        }
    }

    public Account getAccount(Account.Type type, UUID uuid) {
        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.open()) {
            var accountRepository = new AccountRepository(connection);
            return accountRepository.getAccount(type, uuid);
        }
    }

    public UUID reallocateFunds(Account fromAccount, double fromAmount, Account toAccount, double toAmount) {
        UUID eventUuid = UUID.randomUUID();
        Event event = Event.builder().uuid(eventUuid).type(Event.Type.REALLOCATION).build();

        Transaction fromTransaction = switch (fromAccount.getType()) {
            case CURRENT -> CurrentAccountTransaction.builder()
                    .uuid(UUID.randomUUID())
                    .currentAccountUuid(fromAccount.getUuid())
                    .eventUuid(event.getUuid())
                    .amount(-fromAmount)
                    .build();
            case TRADING -> TradingAccountFundTransaction.builder()
                    .uuid(UUID.randomUUID())
                    .tradingAccountUuid(fromAccount.getUuid())
                    .eventUuid(event.getUuid())
                    .amount(-fromAmount)
                    .build();
            default -> throw new InvalidParameterException();
        };

        Transaction toTransaction = switch (toAccount.getType()) {
            case CURRENT -> CurrentAccountTransaction.builder()
                    .uuid(UUID.randomUUID())
                    .currentAccountUuid(toAccount.getUuid())
                    .eventUuid(event.getUuid())
                    .amount(toAmount)
                    .build();
            case TRADING -> TradingAccountFundTransaction.builder()
                    .uuid(UUID.randomUUID())
                    .tradingAccountUuid(toAccount.getUuid())
                    .eventUuid(event.getUuid())
                    .amount(toAmount)
                    .build();

            default -> throw new InvalidParameterException();
        };

        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.beginTransaction()) {
            var eventRepository = new EventRepository(connection);
            var accountRepository = new AccountRepository(connection);
            var transactionRepository = new TransactionRepository(connection);

            fromAccount = accountRepository.getAccount(fromAccount.getType(), fromAccount.getUuid());
            toAccount = accountRepository.getAccount(toAccount.getType(), toAccount.getUuid());

            accountRepository.adjustCurrentAmount(fromAccount, fromAmount, false);
            accountRepository.adjustCurrentAmount(toAccount, toAmount, true);

            eventRepository.add(event);
            transactionRepository.add(fromTransaction);
            transactionRepository.add(toTransaction);

            connection.commit();
        }

        return eventUuid;
    }

    public UUID adjustFunds(Account.Type type, UUID uuid, double amount) {
        UUID eventUuid = UUID.randomUUID();
        Event.Type eventType = amount < 0 ? Event.Type.ADJUSTMENT_REMOVE_BALANCE :Event.Type.ADJUSTMENT_ADD_BALANCE;
        Event event = Event.builder().uuid(eventUuid).type(eventType).build();

        Transaction transaction = switch (type) {
            case CURRENT -> CurrentAccountTransaction.builder()
                    .uuid(UUID.randomUUID())
                    .currentAccountUuid(uuid)
                    .eventUuid(event.getUuid())
                    .amount(amount)
                    .build();
            case TRADING -> TradingAccountFundTransaction.builder()
                    .uuid(UUID.randomUUID())
                    .tradingAccountUuid(uuid)
                    .eventUuid(event.getUuid())
                    .amount(amount)
                    .build();
            default -> throw new InvalidParameterException();
        };

        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.beginTransaction()) {
            var eventRepository = new EventRepository(connection);
            var accountRepository = new AccountRepository(connection);
            var transactionRepository = new TransactionRepository(connection);

            Account account = accountRepository.getAccount(type, uuid);

            if(account == null) {
                throw new InvalidParameterException("Account not found.");
            }

            eventRepository.add(event);
            transactionRepository.add(transaction);
            accountRepository.adjustCurrentAmount(account, Math.abs(amount), amount > 0);

            connection.commit();
        }

        return eventUuid;
    }
}
