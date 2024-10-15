package com.mysite.customers.dto.validation;

/*
 * Ideally this DTO would also contain a specific constant error code, which would be described in a separate resource file.
 */
public record ValidationError(
        String propertyName,
        String propertyValue,
        String errorMessage) { }
