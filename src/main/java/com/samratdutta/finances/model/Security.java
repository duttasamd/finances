package com.samratdutta.finances.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Security {
    enum Type {
        STOCK,
        BOND,
        ETF,
        CFD,
        CD,
        INDEX
    };
    private UUID uuid;
    private String symbol;
    private String name;
    private String exchange;
    private double LTP;
    private Date lastRefreshAt;
    private double previousClose;
    private double change;
    private double changePercent;

}
