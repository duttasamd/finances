package com.samratdutta.finances.api;

import com.samratdutta.finances.model.Expenditure;
import com.samratdutta.finances.model.TradingAccountTransaction;
import com.samratdutta.finances.service.ExpenditureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RestController
public class ExpenditureController {
    @Autowired
    private ExpenditureService expenditureService;

    @PutMapping(value = "/current/{currentAccountUuid}/buy", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID pay(@PathVariable UUID currentAccountUuid, @RequestBody Expenditure expenditure) {
        LOGGER.info("PUT /current/{}/buy : {}", currentAccountUuid, expenditure);

        try {
            return expenditureService.buy(currentAccountUuid, expenditure);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
