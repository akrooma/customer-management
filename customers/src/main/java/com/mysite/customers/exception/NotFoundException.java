package com.mysite.customers.exception;

public class NotFoundException extends Exception {
    public NotFoundException(Long id, String entityName) {
        super(String.format("Entity '%s' was not found with id: %d.", entityName, id));
    }
}
