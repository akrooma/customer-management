package com.mysite.customers.service.customer;

import com.mysite.customers.ExpectedErrorMessages;
import com.mysite.customers.ExpectedPropertyNames;
import com.mysite.customers.TestHelpers;
import com.mysite.customers.dto.CustomerDataDto;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.service.ICustomerService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test cases for {@link ICustomerService#update(Long, CustomerDataDto)}.
 */
@SpringBootTest
public class UpdateCustomerServiceTests {

    @Autowired
    private ICustomerService customerService;

    /**
     * Only last name and e-mail are changed.
     * Checks if first name stays the same and other props change value.
     */
    @Test
    @Order(1)
    void givenNewValidCustomerData_whenUpdate_thenCustomerDataIsUpdated() throws ValidationException, NotFoundException {
        var newCustomer = customerService.create(TestHelpers.getValidCustomerData());

        assertThat(newCustomer).isNotNull();

        var updatedCustomer = customerService.update(newCustomer.id(), new CustomerDataDto(newCustomer.firstName(), "new" + newCustomer.lastName(), "new" + newCustomer.email()));

        assertThat(updatedCustomer).isNotNull();

        assertThat(updatedCustomer.id()).isEqualTo(newCustomer.id());
        assertThat(updatedCustomer.firstName()).isEqualTo(newCustomer.firstName());

        assertThat(updatedCustomer.lastName()).isNotEqualTo(newCustomer.lastName());
        assertThat(updatedCustomer.email()).isNotEqualTo(newCustomer.email());
    }

    /**
     * Also validates the error message.
     */
    @Test
    @Order(2)
    void givenUnknownCustomerId_whenUpdate_thenNotFoundExceptionIsThrown() {
        var unknownCustomerId = -1L;

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> customerService.update(unknownCustomerId, TestHelpers.getValidCustomerData()))
                .withMessage(TestHelpers.getNotFoundExceptionMessage(unknownCustomerId));
    }


    /**
     * Also checks that the validation errors have correct content.
     */
    @Test
    @Order(3)
    void givenCustomerDataNullValues_whenUpdate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto(null, null, null);

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.update(1L, newCustomerData));

        assertThat(validationException).isNotNull();

        var validationErrors = validationException.getErrors();

        assertThat(validationErrors).hasSize(3);

        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.REQUIRED);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.REQUIRED);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.REQUIRED);
    }

    /**
     * Also checks that the validation errors have correct content.
     */
    @Test
    @Order(4)
    void givenCustomerDataEmptyStrings_whenUpdate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto("", "", "");

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.update(1L, newCustomerData));

        assertThat(validationException).isNotNull();

        var validationErrors = validationException.getErrors();

        assertThat(validationErrors).hasSize(3);

        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.SIZE);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.SIZE);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.SIZE);
    }

    /**
     * Also checks that the validation errors have correct content.
     */
    @Test
    @Order(5)
    void givenCustomerDataStringsTooShort_whenUpdate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto("", "", "1234");

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.update(1L, newCustomerData));

        assertThat(validationException).isNotNull();

        var validationErrors = validationException.getErrors();

        assertThat(validationErrors).hasSize(3);

        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.SIZE);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.SIZE);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.SIZE);
    }

    /**
     * Also checks that the validation errors have correct content.
     */
    @Test
    @Order(6)
    void givenCustomerDataStringsTooLong_whenUpdate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto("1".repeat(76), "1".repeat(51), "1".repeat(321));

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.update(1L, newCustomerData));

        assertThat(validationException).isNotNull();

        var validationErrors = validationException.getErrors();

        assertThat(validationErrors).hasSize(3);

        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.SIZE);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.SIZE);
        TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.SIZE);
    }

    /**
     * Also checks that the validation errors have correct content.
     */
    @Test
    @Order(7)
    void givenIncorrectEmailValues_whenUpdate_thenValidationExceptionIsThrown() {
        var validCustomerData = TestHelpers.getValidCustomerData();

        var invalidEmails = TestHelpers.getInvalidEmailValueExamples();

        for (String invalidEmail : invalidEmails) {
            var newCustomerData = new CustomerDataDto(validCustomerData.firstName(), validCustomerData.lastName(), invalidEmail);

            var validationException = assertThrows(
                    ValidationException.class,
                    () -> customerService.update(1L, newCustomerData));

            assertThat(validationException).isNotNull();

            var validationErrors = validationException.getErrors();

            assertThat(validationErrors).hasSize(1);

            TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.FORMAT);
        }
    }

    @Test
    @Order(8)
    void givenNullCustomerData_whenUpdate_thenIllegalArgumentExceptionIsThrown() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customerService.update(1L, null));
    }
}
