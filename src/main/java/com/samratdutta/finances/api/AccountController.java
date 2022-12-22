package com.samratdutta.finances.api;

import com.samratdutta.finances.api.model.ReallocateFund;
import com.samratdutta.finances.model.*;
import com.samratdutta.finances.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PutMapping(value = "/current", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID addCurrentAccount(@RequestBody CurrentAccount currentAccount) {
        LOGGER.info("PUT /account/current : {}", currentAccount);
        try {
            return accountService.addAccount(currentAccount);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping(value = "/fixed_deposit", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID addFixedDepositAccount(@RequestBody FixedDepositAccount fixedDepositAccount) {
        LOGGER.info("PUT /account/fixed_deposit : {}",fixedDepositAccount);
        try {
            return accountService.addAccount(fixedDepositAccount);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping(value = "/trading", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID addTradingAccount(@RequestBody TradingAccount tradingAccount) {
        LOGGER.info("PUT /account/trading : {}", tradingAccount);
        try {
            return accountService.addAccount(tradingAccount);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/{accountType}")
    public List<? extends Account> getAccounts(@PathVariable(required = true) String accountType) {
        Account.Type type = null;
        if(accountType != null) {
            type = switch (accountType) {
                case "current" -> Account.Type.CURRENT;
                case "fixed_deposit" -> Account.Type.FIXED_DEPOSIT;
                case "trading" -> Account.Type.TRADING;
                default -> throw new InvalidParameterException();
            };
        }

        return accountService.getAccounts(type);
    }

    @GetMapping("/")
    public Map<Account.Type, List<? extends Account>> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/{accountType}/{uuid}")
    public Account getAccount(@PathVariable(required = true) String accountType, @PathVariable(required = true) UUID uuid) {
        Account.Type type;

        type = switch (accountType) {
            case "current" -> Account.Type.CURRENT;
            case "fixed_deposit" -> Account.Type.FIXED_DEPOSIT;
            case "trading" -> Account.Type.TRADING;
            default -> throw new InvalidParameterException();
        };

        return accountService.getAccount(type, uuid);
    }

    @PostMapping(value = "/reallocate", consumes = {"application/json"})
    public UUID reallocateFunds(@RequestBody ReallocateFund reallocateFund) {
        Account fromAccount = switch (reallocateFund.getFromType()) {
            case CURRENT -> CurrentAccount.builder().uuid(reallocateFund.getFromUuid()).build();
            case FIXED_DEPOSIT -> FixedDepositAccount.builder().uuid(reallocateFund.getFromUuid()).build();
            case TRADING -> TradingAccount.builder().uuid(reallocateFund.getFromUuid()).build();
        };

        Account toAccount = switch (reallocateFund.getToType()) {
            case CURRENT -> CurrentAccount.builder().uuid(reallocateFund.getToUuid()).build();
            case TRADING -> TradingAccount.builder().uuid(reallocateFund.getToUuid()).build();
            default -> throw new InvalidParameterException();
        };

        return accountService.reallocateFunds(fromAccount, reallocateFund.getFromAmount(), toAccount, reallocateFund.getToAmount());
    }
}
