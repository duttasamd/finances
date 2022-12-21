package com.samratdutta.finances.model;

import java.util.Date;
import java.util.UUID;

public class CurrentAccountTransaction extends Transaction {
    private UUID currentAccountUuid;
    private UUID currentAccountTransactionTypeUuid;
}
