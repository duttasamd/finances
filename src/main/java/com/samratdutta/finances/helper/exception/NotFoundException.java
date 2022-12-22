package com.samratdutta.finances.helper.exception;

import java.util.UUID;

public class NotFoundException extends Exception {
    public NotFoundException(String entity, UUID uuid) {
        super(entity + " : " + uuid.toString() + " NOT FOUND.");
    }
}
