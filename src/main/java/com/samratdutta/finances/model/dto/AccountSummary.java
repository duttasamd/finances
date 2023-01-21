package com.samratdutta.finances.model.dto;

import com.samratdutta.finances.model.Account;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AccountSummary {
    private double fixedBalance;
    private double currentBalance;
    private double tradingBalance;
    Map<Account.Type, List<? extends Account>> accountMap;
}
