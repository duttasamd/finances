package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.Budget;
import com.samratdutta.finances.model.Expenditure;
import com.samratdutta.finances.model.dto.ExpenditureDTO;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;

import java.util.List;
import java.util.UUID;

@Slf4j
public class ExpenditureRepository {
    private Connection connection;
    public ExpenditureRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Expenditure expenditure) {
        String queryText = "INSERT INTO expenditure(uuid, eventUuid, type, amount, " +
                "currency, comment, timestamp) " +
                "VALUES(:uuidStr, :eventUuidStr, :type, :amount, :currency, :comment, :timestamp)";

        try(var query = connection.createQuery(queryText)) {
            query.bind(expenditure);
            query.addParameter("uuidStr", expenditure.getUuid().toString());
            query.addParameter("eventUuidStr", expenditure.getEventUuid().toString());
            query.addParameter("timestamp", expenditure.getTimestamp());
            query.executeUpdate();
        }
    }

    public List<ExpenditureDTO> list(int year, int month, Budget.Type budgetType) {
        String queryText = "SELECT current_account.name as account, expenditure.* from expenditure " +
                "JOIN current_account_transaction ON current_account_transaction.event_uuid = expenditure.eventUuid " +
                "JOIN current_account ON current_account.uuid = current_account_transaction.current_account_uuid " +
                "JOIN budget ON budget.uuid = expenditure.budgetUuid " +
                "WHERE MONTH(expenditure.timestamp) = :month AND YEAR(expenditure.timestamp) = :year " +
                "AND budget.type = :budgetType " +
                "ORDER BY expenditure.timestamp DESC";

        LOGGER.info(queryText + "{} {} {}", year, month, budgetType.name());

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("month", month);
            query.addParameter("year", year);
            query.addParameter("budgetType", budgetType.name());

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
