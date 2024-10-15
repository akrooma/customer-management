package com.mysite.customers.dto;

import com.mysite.customers.domain.config.CustomerPropertyConfiguration;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDto(
        Long id,

        @NotNull
        @Size(min = CustomerPropertyConfiguration.FirstName.MIN_SIZE, max = CustomerPropertyConfiguration.FirstName.MAX_SIZE)
        String firstName,

        @NotNull
        @Size(min = CustomerPropertyConfiguration.LastName.MIN_SIZE, max = CustomerPropertyConfiguration.LastName.MAX_SIZE)
        String lastName,

        @NotNull
        @Size(min = CustomerPropertyConfiguration.Email.MIN_SIZE, max = CustomerPropertyConfiguration.Email.MAX_SIZE)
        String email) { }
