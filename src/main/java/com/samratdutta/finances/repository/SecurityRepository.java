package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.Security;
import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;

@Slf4j
public class SecurityRepository {
    private Connection connection;
    public SecurityRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Security security) {
        String queryText = "INSERT INTO security(uuid, name, symbol, exchange, " +
                "last_refreshed_at, LTP, previous_close, `change`, change_percent) " +
                "VALUES(:uuidStr, :name, :symbol, :exchange, " +
                "NOW(), :LTP, :previousClose, :change, :changePercent)";

        try(var query = connection.createQuery(queryText)) {
            query.bind(security);
            query.addParameter("uuidStr", security.getUuid().toString());
            query.executeUpdate();
        }
    }
}
