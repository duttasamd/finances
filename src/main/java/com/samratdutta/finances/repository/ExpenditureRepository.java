package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.Expenditure;
import com.samratdutta.finances.model.dto.ExpenditureDTO;
import org.sql2o.Connection;

import java.util.List;
import java.util.UUID;

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

    public List<ExpenditureDTO> list(int year, int month) {
        String queryText = "SELECT current_account.name as account, expenditure.* from expenditure " +
                "JOIN current_account_transaction ON current_account_transaction.event_uuid = expenditure.eventUuid " +
                "JOIN current_account ON current_account.uuid = current_account_transaction.current_account_uuid " +
                "WHERE MONTH(expenditure.timestamp) = :month AND YEAR(expenditure.timestamp) = :year " +
                "ORDER BY expenditure.timestamp DESC";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("month", month);
            query.addParameter("year", year);

            return query.executeAndFetch(ExpenditureDTO.class);
        }
    }

    public Expenditure get(UUID uuid) {
        String queryText = "SELECT * FROM expenditure WHERE uuid = :uuidStr";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", uuid.toString());

            return query.executeAndFetchFirst(Expenditure.class);
        }
    }

    public void remove(UUID uuid) {
        String queryText = "DELETE FROM expenditure WHERE uuid = :uuidStr";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", uuid.toString());
            query.executeUpdate();
        }
    }
}
