package com.samratdutta.finances.service;

import com.samratdutta.finances.model.Account;
import com.samratdutta.finances.model.CurrentAccount;
import com.samratdutta.finances.model.FixedDepositAccount;
import com.samratdutta.finances.model.TradingAccount;
import com.samratdutta.finances.repository.AccountRepository;
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
}
