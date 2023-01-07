package com.samratdutta.finances.service;

import com.samratdutta.finances.model.BudgetEntry;
import com.samratdutta.finances.model.Expenditure;
import com.samratdutta.finances.repository.BudgetRepository;
import com.samratdutta.finances.repository.ExpenditureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
public class BudgetService {
    @Autowired
    private DataSource dataSource;

    public List<BudgetEntry> list(int year, int month) {
        Sql2o financesDb = new Sql2o(dataSource);
        try(Connection connection = financesDb.open()) {
            var budgetRepository = new BudgetRepository(connection);
            return budgetRepository.list(year, month);
        }
    }
}
