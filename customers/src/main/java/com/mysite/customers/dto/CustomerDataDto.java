package com.mysite.customers.dto;

import com.mysite.customers.domain.config.CustomerPropertyConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDataDto(
        @NotNull
        @Size(min = CustomerPropertyConfiguration.FirstName.MIN_SIZE, max = CustomerPropertyConfiguration.FirstName.MAX_SIZE)
        @Schema(description = "Must be a single word, contain only letters, only first letter can be uppercase")
        String firstName,

        @NotNull
        @Size(min = CustomerPropertyConfiguration.LastName.MIN_SIZE, max = CustomerPropertyConfiguration.LastName.MAX_SIZE)
        @Schema(description = "Must be a single word, contain only letters, only first letter can be uppercase")
        String lastName,

        @NotNull
        @Size(min = CustomerPropertyConfiguration.Email.MIN_SIZE, max = CustomerPropertyConfiguration.Email.MAX_SIZE)
        @Schema(description = "Must be a valid e-mail address that matches the following RegEx pattern: ^[a-z]+@[a-z]+\\.[a-z]+$")
        String email) { }
