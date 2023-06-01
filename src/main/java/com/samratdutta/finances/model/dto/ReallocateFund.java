package com.samratdutta.finances.model.dto;

import com.samratdutta.finances.model.Account;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Data
public class ReallocateFund {
    private Account.Type fromType;
    private UUID fromUuid;
    private Account.Type toType;
    private UUID toUuid;
    private double fromAmount;
    private double toAmount;

    @DateTimeFormat(pattern= "yyyy-MM-dd")
    private Optional<Date> timestamp;

}
