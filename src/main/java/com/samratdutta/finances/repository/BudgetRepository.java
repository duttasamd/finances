package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.BudgetEntry;
import com.samratdutta.finances.model.Expenditure;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
public class BudgetRepository {
    private Connection connection;
    public BudgetRepository(Connection connection) {
        this.connection = connection;
    }
    public List<BudgetEntry> list(int year, int month) {
        String queryText = "SELECT budget_entry.type, budget_entry.amount from " +
                "budget JOIN budget_entry on budget_entry.budget_uuid = budget.uuid " +
                "WHERE :date >= start AND (end is NULL OR end <= :date)";

        try(var query = connection.createQuery(queryText)) {
            Date date = new GregorianCalendar(year, month - 1, 01).getTime();
            query.addParameter("date", date);

            return query.executeAndFetch(BudgetEntry.class);
        }
    }
}
