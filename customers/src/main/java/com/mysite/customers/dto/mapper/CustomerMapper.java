package com.mysite.customers.dto.mapper;

import com.mysite.customers.domain.Customer;
import com.mysite.customers.dto.CustomerDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

public class CustomerMapper {
    public static CustomerDto toDto(@NotNull Customer customer) throws IllegalArgumentException {
        Assert.notNull(customer, "Customer cannot be null");

        return new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }
}
