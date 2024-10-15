package com.mysite.customers.domain;

import com.mysite.customers.exception.ValidationException;

public class CustomerFactory {
    public static Customer createCustomer(String firstName, String lastName, String email) throws ValidationException {
        CustomerValidator.ensureValidity(firstName, lastName, email);

        return new Customer(firstName, lastName, email);
    }
}
