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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test cases for {@link ICustomerService#create(CustomerDataDto)}.
 */
@SpringBootTest
public class CreateCustomerServiceTests {

    @Autowired
    private ICustomerService customerService;

    @Test
    @Order(1)
    void givenValidCustomerData_whenCreate_thenReturnNewCustomerDataDto() throws ValidationException {
        var newCustomerData = TestHelpers.getValidCustomerData();

        var newCustomer = customerService.create(newCustomerData);

        assertThat(newCustomer).isNotNull();

        assertThat(newCustomer.id()).isGreaterThan(0L);

        assertThat(newCustomer.firstName()).isEqualTo(newCustomerData.firstName());
        assertThat(newCustomer.lastName()).isEqualTo(newCustomerData.lastName());
        assertThat(newCustomer.email()).isEqualTo(newCustomerData.email());
    }

    /**
     * Also checks that the validation errors have correct content.
     */
    @Test
    @Order(2)
    void givenCustomerDataNullValues_whenCreate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto(null, null, null);

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.create(newCustomerData));

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
    @Order(3)
    void givenCustomerDataEmptyStrings_whenCreate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto("", "", "");

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.create(newCustomerData));

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
    @Order(4)
    void givenCustomerDataStringsTooShort_whenCreate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto("", "", "1234");

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.create(newCustomerData));

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
    void givenCustomerDataStringsTooLong_whenCreate_thenValidationExceptionIsThrown() {
        var newCustomerData = new CustomerDataDto("1".repeat(76), "1".repeat(51), "1".repeat(321));

        var validationException = assertThrows(
                ValidationException.class,
                () -> customerService.create(newCustomerData));

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
    void givenIncorrectEmailValues_whenCreate_thenValidationExceptionIsThrown() {
        var validCustomerData = TestHelpers.getValidCustomerData();

        var invalidEmails = TestHelpers.getInvalidEmailValueExamples();

        for (String invalidEmail : invalidEmails) {
            var newCustomerData = new CustomerDataDto(validCustomerData.firstName(), validCustomerData.lastName(), invalidEmail);

            var validationException = assertThrows(
                    ValidationException.class,
                    () -> customerService.create(newCustomerData));

            assertThat(validationException).isNotNull();

            var validationErrors = validationException.getErrors();

            assertThat(validationErrors).hasSize(1);

            TestHelpers.hasValidationError(validationErrors, ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.FORMAT);
        }
    }

    @Test
    @Order(7)
    void givenNullCustomerData_whenCreate_thenIllegalArgumentExceptionIsThrown() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customerService.create(null));
    }
}
