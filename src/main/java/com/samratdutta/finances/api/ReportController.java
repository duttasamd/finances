package com.samratdutta.finances.api;

import com.samratdutta.finances.model.Account;
import com.samratdutta.finances.model.dto.DailySummary;
import com.samratdutta.finances.service.AccountService;
import com.samratdutta.finances.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/{accountType}/{uuid}/daily-total-map")
    public List<DailySummary> getDailyTotalSummary(@PathVariable(required = true) Account.Type accountType,
                                                   @PathVariable(required = true) UUID uuid,
                                                   LocalDate fromDate, LocalDate toDate) {
        return reportService.getDailyTotalSummary(accountType,uuid,fromDate,toDate);
    }
}
