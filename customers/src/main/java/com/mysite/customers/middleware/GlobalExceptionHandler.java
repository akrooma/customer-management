package com.mysite.customers.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.customers.dto.validation.ValidationErrorsDto;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex, WebRequest webRequest) {
        /*
         * In a more complicated application you would log more request data with the exception.
         */
        logger.error("Not found exception: {}. Request: {}", ex.getMessage(), webRequest.toString());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .body(ex.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ValidationErrorsDto> handleValidationException(ValidationException ex, WebRequest webRequest) throws JsonProcessingException {
        var errorsStr = new ObjectMapper().writeValueAsString(ex.getErrors());

        /*
         * In a more complicated application you would log more request data with the exception.
         */
        logger.error("Validation exception: {}. Request: {}", errorsStr, webRequest.toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ValidationErrorsDto(ex.getErrors()));
    }

}
