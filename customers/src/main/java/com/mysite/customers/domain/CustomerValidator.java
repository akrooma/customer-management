package com.mysite.customers.domain;

import com.mysite.customers.domain.config.CustomerPropertyConfiguration;
import com.mysite.customers.dto.validation.ValidationError;
import com.mysite.customers.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerValidator {
    /**
     * For simplicity the pattern is: "^[a-z]+@[a-z]+\.[a-z]+$" (without the quotation marks).
     * Explanation: string must contain the symbol @;
     * the text before @ can contain only lowercase letters (no "special" letters however), with at least one letter being mandatory;
     * there must be text after @, it can contain only lowercase letters (no "special" letters however) and a single period, the period has to be surrounded by letters.
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-z]+@[a-z]+\\.[a-z]+$");

    protected static void ensureValidity(String firstName, String lastName, String email) throws ValidationException {
        var validationErrors = getValidationErrors(firstName, lastName, email);

        if (validationErrors.isEmpty()) return;

        throw new ValidationException(validationErrors);
    }

    private static List<ValidationError> getValidationErrors(String firstName, String lastName, String email) {
        List<ValidationError> validationErrors = new ArrayList<>(0);

        validationErrors.addAll(validateFirstName(firstName));

        validationErrors.addAll(validateLastName(lastName));

        validationErrors.addAll(validateEmail(email));

        return validationErrors;
    }

    /*
     * String props validations could be made more "generic" with a prop validation context data structure input.
     */

    private static List<ValidationError> validateFirstName(String firstName) {
        List<ValidationError> firstNameValidationErrors = new ArrayList<>(0);

        if (firstName == null) {
            firstNameValidationErrors.add(new ValidationError(Customer.FIRST_NAME_PROP_NAME, null, "First name must have a value."));

            return firstNameValidationErrors;
        }

        if (firstName.length() < CustomerPropertyConfiguration.FirstName.MIN_SIZE
                || firstName.length() > CustomerPropertyConfiguration.FirstName.MAX_SIZE) {
            firstNameValidationErrors.add(new ValidationError(
                    Customer.FIRST_NAME_PROP_NAME,
                    firstName,
                    String.format("First name must be between %d and %d characters long.", CustomerPropertyConfiguration.FirstName.MIN_SIZE, CustomerPropertyConfiguration.FirstName.MAX_SIZE)));
        }

        return firstNameValidationErrors;
    }

    private static List<ValidationError> validateLastName(String lastName) {
        List<ValidationError> lastNameValidationErrors = new ArrayList<>(0);

        if (lastName == null) {
            lastNameValidationErrors.add(new ValidationError(Customer.LAST_NAME_PROP_NAME, null, "Last name must have a value."));

            return lastNameValidationErrors;
        }

        if (lastName.length() < CustomerPropertyConfiguration.LastName.MIN_SIZE
                || lastName.length() > CustomerPropertyConfiguration.LastName.MAX_SIZE) {
            lastNameValidationErrors.add(new ValidationError(
                    Customer.LAST_NAME_PROP_NAME,
                    lastName,
                    String.format("Last name must be between %d and %d characters long.", CustomerPropertyConfiguration.LastName.MIN_SIZE, CustomerPropertyConfiguration.LastName.MAX_SIZE)));
        }

        return lastNameValidationErrors;
    }

    private static List<ValidationError> validateEmail(String email) {
        List<ValidationError> emailValidationErrors = new ArrayList<>(0);

        if (email == null) {
            emailValidationErrors.add(new ValidationError(Customer.EMAIL_PROP_NAME, null, "E-mail must have a value."));

            return emailValidationErrors;
        }

        if (email.length() < CustomerPropertyConfiguration.Email.MIN_SIZE
                || email.length() > CustomerPropertyConfiguration.Email.MAX_SIZE) {
            emailValidationErrors.add(new ValidationError(
                    Customer.EMAIL_PROP_NAME,
                    email,
                    String.format("E-mail must be between %d and %d characters long.", CustomerPropertyConfiguration.Email.MIN_SIZE, CustomerPropertyConfiguration.Email.MAX_SIZE)));
        } else if (!EMAIL_REGEX.matcher(email).matches()) {
            emailValidationErrors.add(new ValidationError(
                    Customer.EMAIL_PROP_NAME,
                    email,
                    "E-mail has an incorrect format. See the API documentation for the correct format."));
        }

        return emailValidationErrors;
    }
}
