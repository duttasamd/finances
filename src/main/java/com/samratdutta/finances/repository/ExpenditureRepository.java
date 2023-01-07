package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.Expenditure;
import org.sql2o.Connection;

import java.util.List;

public class ExpenditureRepository {
    private Connection connection;
    public ExpenditureRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Expenditure expenditure) {
        String queryText = "INSERT INTO expenditure(uuid, eventUuid, type, amount, " +
                "currency, comment, timestamp) " +
                "VALUES(:uuidStr, :eventUuidStr, :type, :amount, :currency, :comment, NOW())";

        try(var query = connection.createQuery(queryText)) {
            query.bind(expenditure);
            query.addParameter("uuidStr", expenditure.getUuid().toString());
            query.addParameter("eventUuidStr", expenditure.getEventUuid().toString());
            query.executeUpdate();
        }
    }

    public List<Expenditure> list(int year, int month) {
        String queryText = "SELECT * from expenditure WHERE MONTH(timestamp) = :month AND YEAR(timestamp) = :year";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("month", month);
            query.addParameter("year", year);

            return query.executeAndFetch(Expenditure.class);
        }
    }
}
