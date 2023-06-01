package com.samratdutta.finances.api;

import com.samratdutta.finances.helper.exception.NotFoundException;
import com.samratdutta.finances.model.Budget;
import com.samratdutta.finances.model.BudgetEntry;
import com.samratdutta.finances.model.Expenditure;
import com.samratdutta.finances.model.dto.BudgetExpenditure;
import com.samratdutta.finances.model.dto.ExpenditureDTO;
import com.samratdutta.finances.model.dto.ExpenditureSummary;
import com.samratdutta.finances.service.BudgetService;
import com.samratdutta.finances.service.ExpenditureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExpenditureController {
    @Autowired
    private ExpenditureService expenditureService;
    @Autowired
    private BudgetService budgetService;

    @PutMapping(value = "/current/{currentAccountUuid}/buy", consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public UUID pay(@PathVariable UUID currentAccountUuid, @RequestBody Expenditure expenditure) {
        LOGGER.info("PUT /current/{}/buy : {}", currentAccountUuid, expenditure);

        try {
            if(expenditure.getTimestamp() == null)
                expenditure.setTimestamp(new Date());
            return expenditureService.buy(currentAccountUuid, expenditure);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @GetMapping("/expenditure/{year}/{month}")
    public List<BudgetExpenditure> getMonthlyExpenditureMap(@PathVariable int year, @PathVariable int month) {
        LOGGER.info("GET /expenditure/{}/{}", year, month);
        List<BudgetExpenditure> budgetExpenditures = new ArrayList<>();

        try {
            Map<Expenditure.Type, List<ExpenditureDTO>> expenditureMap = expenditureService.list(year, month, Budget.Type.MONTHLY);
            List<BudgetEntry> budgetEntries = budgetService.list(year, month);

            for (BudgetEntry budgetEntry : budgetEntries) {
                List<ExpenditureDTO> expenditures = expenditureMap.get(budgetEntry.getType());
                if(expenditures == null)
                    expenditures = new ArrayList<>();

                double amountSpent = expenditures.stream().mapToDouble(ExpenditureDTO::getAmount).sum();

                BudgetExpenditure budgetExpenditure =
                        BudgetExpenditure.builder()
                                .type(budgetEntry.getType())
                                .budget(budgetEntry.getAmount())
                                .spent(amountSpent)
                                .expenditures(expenditures)
                        .build();

                budgetExpenditures.add(budgetExpenditure);
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return budgetExpenditures;
    }

    @GetMapping("/expenditure/summary/{year}/{month}")
    public ExpenditureSummary getMonthlyExpenditureSummary(@PathVariable int year, @PathVariable int month) {
        LOGGER.info("GET /expenditure/{}/{}", year, month);

        return expenditureService.getExpenditureSummary(year, month, Budget.Type.MONTHLY);
    }

    @DeleteMapping("/expenditure/{uuid}")
    public boolean removeExpenditure(@PathVariable UUID uuid) throws NotFoundException {
        return expenditureService.removeExpenditure(uuid);
    }
}
