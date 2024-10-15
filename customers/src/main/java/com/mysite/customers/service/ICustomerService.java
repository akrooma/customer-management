package com.mysite.customers.service;

import com.mysite.customers.dto.CustomerDataDto;
import com.mysite.customers.dto.CustomerDto;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import jakarta.validation.constraints.NotNull;

public interface ICustomerService {
    /**
     * Tries to find a customer by its ID.
     * @param id customer's ID
     * @return customer DTO
     * @throws NotFoundException if customer was not found
     */
    CustomerDto findById(long id) throws NotFoundException;

    /**
     * Tries to create a new customer.
     * @param newCustomerDataDto new customer data
     * @return DTO for new customer entity
     * @throws IllegalArgumentException if customer data DTO is null
     * @throws ValidationException if any of the customer properties do not pass business and/or data model restriction validations
     */
    CustomerDto create(@NotNull CustomerDataDto newCustomerDataDto) throws IllegalArgumentException, ValidationException;

    /**
     * Tires to update an existing customer.
     * @param id customer's ID
     * @param updatedCustomerDataDto updated customer data
     * @return DTO for updated customer entity
     * @throws IllegalArgumentException if customer data DTO is null
     * @throws NotFoundException if customer to be updated was not found
     * @throws ValidationException if any of the customer properties do not pass business and/or data model restriction validations
     */
    CustomerDto update(long id, @NotNull CustomerDataDto updatedCustomerDataDto) throws IllegalArgumentException, NotFoundException, ValidationException;

    /**
     * Tries to delete a customer by its ID.
     * @param id customer's ID
     * @throws NotFoundException if customer to be deleted was not found
     */
    void deleteById(long id) throws NotFoundException;
}
