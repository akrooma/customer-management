package com.mysite.customers.exception;

import com.mysite.customers.dto.validation.ValidationError;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends Exception {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        this.errors = errors;
    }

}
