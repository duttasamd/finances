package com.samratdutta.finances.model.dto;

import com.samratdutta.finances.model.Account;
import lombok.Data;

import java.util.UUID;

@Data
public class ReallocateFund {
    private Account.Type fromType;
    private UUID fromUuid;
    private Account.Type toType;
    private UUID toUuid;
    private double fromAmount;
    private double toAmount;

}
