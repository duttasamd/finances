package com.samratdutta.finances.repository;

import com.samratdutta.finances.model.Event;
import org.sql2o.Connection;

import java.util.UUID;

public class EventRepository {
    private Connection connection;
    public EventRepository(Connection connection) {
        this.connection = connection;
    }

    public void add(Event event) {
        String queryText = "INSERT INTO event(uuid, type, timestamp) " +
                "VALUES(:uuidStr, :type, NOW())";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", event.getUuid().toString());
            query.addParameter("type", event.getType().name());
            query.executeUpdate();
        }
    }

    public void remove(UUID uuid) {
        String queryText = "DELETE FROM event WHERE uuid = :uuidStr";

        try(var query = connection.createQuery(queryText)) {
            query.addParameter("uuidStr", uuid.toString());
            query.executeUpdate();
        }
    }
}
