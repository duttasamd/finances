package com.samratdutta.finances.service;

import com.samratdutta.finances.model.Account;
import com.samratdutta.finances.model.dto.DailySummary;
import com.samratdutta.finances.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ReportService {
    @Autowired
    private DataSource dataSource;

    public List<DailySummary> getDailyTotalSummary(Account.Type type, UUID accountUuid, LocalDate fromDate, LocalDate toDate) {
        List<DailySummary> dailyTotalMap;

        if(fromDate == null) {
            fromDate = LocalDate.MIN;
        }

        if(toDate == null) {
            toDate = LocalDate.now();
        }

        if(fromDate.isAfter(toDate)) {
            throw new RuntimeException("fromDate must be same or before toDate");
        }

        Sql2o financesDb = new Sql2o(dataSource);
        try(var connection = financesDb.open()) {
            var transactionRepository = new TransactionRepository(connection);
            dailyTotalMap = transactionRepository.getDailyTotalSummary(type, accountUuid);

            LocalDate finalFromDate = fromDate;
            LocalDate finalToDate = toDate;

            dailyTotalMap = dailyTotalMap.stream().filter(
                    x -> (x.getDate().isEqual(finalFromDate) || x.getDate().isAfter(finalFromDate))
                            &&
                        (x.getDate().isEqual(finalToDate) || x.getDate().isBefore(finalToDate))
            ).toList();
        }

        return dailyTotalMap;
    }
}
