package com.samratdutta.finances.helper.exception;

import com.samratdutta.finances.model.Account;

import java.util.UUID;

public class NotEnoughBalanceException extends Exception {

    public NotEnoughBalanceException(String accountType, String accountName, double transactionValue) {
        super(accountType + " : " + accountName + " not enough balance for transaction worth " + transactionValue);
    }
}
