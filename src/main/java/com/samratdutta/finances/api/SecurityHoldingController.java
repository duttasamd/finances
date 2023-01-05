package com.samratdutta.finances.api;

import com.samratdutta.finances.model.Event;
import com.samratdutta.finances.model.Security;
import com.samratdutta.finances.model.SecurityHolding;
import com.samratdutta.finances.model.TradingAccountTransaction;
import com.samratdutta.finances.service.AccountService;
import com.samratdutta.finances.service.SecurityHoldingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class SecurityHoldingController {
    @Autowired
    private SecurityHoldingService securityHoldingService;

    @PutMapping(value = "/security", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID addSecurity(@RequestBody Security security) {
        LOGGER.info("PUT /security : {}", security);
        try {
            return securityHoldingService.addSecurity(security);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PutMapping(value = "/account/trading/{tradingAccountUuid}/buy", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID buySecurity(@PathVariable UUID tradingAccountUuid, @RequestBody TradingAccountTransaction tradingAccountTransaction) {
        LOGGER.info("PUT /account/trading/{}/buy : {}", tradingAccountUuid, tradingAccountTransaction);
        tradingAccountTransaction.setTradingAccountUuid(tradingAccountUuid);

        try {
            return securityHoldingService.buySecurity(tradingAccountTransaction);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/account/trading/holdings")
    @ResponseStatus(HttpStatus.OK)
    public Map<UUID, List<SecurityHolding>> getSecurityHoldingMap() {
        LOGGER.info("GET /account/trading/holdings");

        try {
            return securityHoldingService.getSecurityHoldingMap();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
