package com.mysite.customers.dto.validation;

import java.util.List;

public record ValidationErrorsDto(List<ValidationError> errors) { }
