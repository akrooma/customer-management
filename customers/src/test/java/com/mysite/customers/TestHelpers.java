package com.mysite.customers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.customers.dto.CustomerDataDto;
import com.mysite.customers.dto.CustomerDto;
import com.mysite.customers.dto.validation.ValidationError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestHelpers {
    /**
     * Constructs a customer data DTO with valid customer entity property values.
     * @return a valid customer data DTO
     */
    public static CustomerDataDto getValidCustomerData() {
        return new CustomerDataDto("John", "Doe", "johndoe@example.com");
    }

    /**
     * Constructs a customer DTO with valid customer entity property values.
     * @return a valid CustomerDto, where the customer ID is 1
     */
    public static CustomerDto getValidCustomerDto() {
        return getCustomerDto(getValidCustomerData());
    }

    /**
     * Constructs a customer DTO from given customer data DTO.
     * @param customerDataDto customer data DTO
     * @return a mapped CustomerDto, where the customer ID is 1
     */
    public static CustomerDto getCustomerDto(CustomerDataDto customerDataDto) {
        return new CustomerDto(1L, customerDataDto.firstName(), customerDataDto.lastName(), customerDataDto.email());
    }

    /**
     * Expected message of a not found exception for given customer entity.
     * @param customerId customer's ID
     * @return not found exception message
     */
    public static String getNotFoundExceptionMessage(long customerId) {
        return String.format("Entity 'Customer' was not found with id: %d.", customerId);
    }

    /**
     * Asserts that the given error collection has an expected error entity for given property name.
     * @param errors error collection
     * @param propertyName property name
     * @param propertyValue expected property value which caused the error
     * @param errorMessage expected error message
     */
    public static void hasValidationError(List<ValidationError> errors, String propertyName, String propertyValue, String errorMessage) {
        var validationError = errors
                .stream()
                .filter(error -> error.propertyName().equals(propertyName))
                .findFirst().orElseThrow(() -> new AssertionError(String.format("No error %s found", propertyName)));

        assertThat(validationError.propertyValue()).isEqualTo(propertyValue);
        assertThat(validationError.errorMessage()).isEqualTo(errorMessage);
    }

    /**
     * Current RegEx pattern: "^[a-z]+@[a-z]+\\.[a-z]+$" (without the quotation marks).
     * Explanation: string must contain the symbol @;
     * the text before @ can contain only lowercase letters (no "special" letters however), with at least one letter being mandatory;
     * there must be text after @, it can contain only lowercase letters (no "special" letters however) and a single period, the period has to be surrounded by letters.
     * Example of a valid e-mail address: johndoe@example.com
     * @return array of invalid e-mail addresses
     */
    public static String[] getInvalidEmailValueExamples() {
        return new String[]{
                "johndoeexample.com" // missing @
                , "@example.com" // missing text before @
                , "johndoe@" // missing text after @
                , "1@example.com" // non-letter (number) before @
                , "ä@example.com" // "special" letter (ä) before @
                , "johndoe@examplecom" // missing period after @
                , "johndoe@example..com" // multiple periods
                , "johndoe@1.com" // non-letter (number) after @
                , "johndoe@ä.com" // "special" letter (ä) after @
                , "johndoe@.com" // no text before period
                , "johndoe@example." // no text after period
                , "johndoe@example.1" // period followed by a non-letter (number)
                , "johndoe@example.öö" // period followed by "special" letters
        };
    }

    /**
     * Converts an object to its JSON string representation.
     * @param object object to convert
     * @return JSON string representation of the given object
     * @throws JsonProcessingException processing exception if object cannot be converted to JSON
     */
    public static String toJsonStr(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
