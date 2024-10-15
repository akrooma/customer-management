package com.mysite.customers.controller.customer;

import com.mysite.customers.ExpectedErrorMessages;
import com.mysite.customers.ExpectedPropertyNames;
import com.mysite.customers.TestHelpers;
import com.mysite.customers.controller.CustomerController;
import com.mysite.customers.dto.CustomerDataDto;
import com.mysite.customers.dto.validation.ValidationError;
import com.mysite.customers.dto.validation.ValidationErrorsDto;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.service.ICustomerService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test cases for {@link CustomerController#createCustomer(CustomerDataDto)}.
 */
@WebMvcTest(CustomerController.class)
public class CreateCustomerEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    @Test
    @Order(1)
    void givenValidCustomerData_whenCreateCustomer_thenStatus201AndCreatedCustomerDataIsReturned() throws Exception {
        var customerDto = TestHelpers.getValidCustomerDto();

        when(customerService.create(any())).thenReturn(customerDto);

        var customerDtoJsonStr = TestHelpers.toJsonStr(customerDto);

        mockMvc.perform(post(ControllerTestsConstants.CUSTOMER_PATH_ROOT)
                        .content(customerDtoJsonStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(customerDtoJsonStr));
    }

    @Test
    @Order(2)
    void givenCustomerDataNullValues_whenCreateCustomer_thenStatus400AndValidationErrorsAreReturned() throws Exception {
        var newCustomerData = new CustomerDataDto(null, null, null);

        List<ValidationError> validationErrors = Arrays.asList(
                new ValidationError(ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.REQUIRED),
                new ValidationError(ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.REQUIRED),
                new ValidationError(ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.REQUIRED)
        );

        testInvalidCustomerDataInputs(newCustomerData, validationErrors);
    }

    @Test
    @Order(3)
    void givenCustomerDataEmptyValues_whenCreateCustomer_thenStatus400AndValidationErrorsAreReturned() throws Exception {
        var newCustomerData = new CustomerDataDto("", "", "");

        List<ValidationError> validationErrors = Arrays.asList(
                new ValidationError(ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.SIZE),
                new ValidationError(ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.SIZE),
                new ValidationError(ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.SIZE)
        );

        testInvalidCustomerDataInputs(newCustomerData, validationErrors);
    }

    @Test
    @Order(4)
    void givenCustomerDataStringsTooShort_whenCreateCustomer_thenStatus400AndValidationErrorsAreReturned() throws Exception {
        var newCustomerData = new CustomerDataDto("", "", "1234");

        List<ValidationError> validationErrors = Arrays.asList(
                new ValidationError(ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.SIZE),
                new ValidationError(ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.SIZE),
                new ValidationError(ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.SIZE)
        );

        testInvalidCustomerDataInputs(newCustomerData, validationErrors);
    }

    @Test
    @Order(5)
    void givenCustomerDataStringsTooLong_whenCreateCustomer_thenStatus400AndValidationErrorsAreReturned() throws Exception {
        var newCustomerData = new CustomerDataDto("1".repeat(76), "1".repeat(51), "1".repeat(321));

        List<ValidationError> validationErrors = Arrays.asList(
                new ValidationError(ExpectedPropertyNames.FIRST_NAME, newCustomerData.firstName(), ExpectedErrorMessages.FirstName.SIZE),
                new ValidationError(ExpectedPropertyNames.LAST_NAME, newCustomerData.lastName(), ExpectedErrorMessages.LastName.SIZE),
                new ValidationError(ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.SIZE)
        );

        testInvalidCustomerDataInputs(newCustomerData, validationErrors);
    }

    @Test
    @Order(6)
    void givenIncorrectEmailValues_whenCreateCustomer_thenStatus400AndValidationErrorsAreReturned() throws Exception {
        var validCustomerData = TestHelpers.getValidCustomerData();

        var invalidEmails = TestHelpers.getInvalidEmailValueExamples();

        for (String invalidEmail : invalidEmails) {
            var newCustomerData = new CustomerDataDto(validCustomerData.firstName(), validCustomerData.lastName(), invalidEmail);

            List<ValidationError> validationErrors = List.of(
                    new ValidationError(ExpectedPropertyNames.EMAIL, newCustomerData.email(), ExpectedErrorMessages.Email.FORMAT)
            );

            testInvalidCustomerDataInputs(newCustomerData, validationErrors);

            reset(customerService); // reset mock service since we are in a loop
        }
    }

    @Test
    @Order(7)
    void givenNullCustomerData_whenCreateCustomer_thenStatus400IsReturned() throws Exception {
        when(customerService.create(any())).thenThrow(new IllegalArgumentException());

        var customerDtoJsonStr = TestHelpers.toJsonStr(null);

        mockMvc.perform(post(ControllerTestsConstants.CUSTOMER_PATH_ROOT)
                        .content(customerDtoJsonStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void testInvalidCustomerDataInputs(CustomerDataDto customerDataDto, List<ValidationError> validationErrors) throws Exception {
        when(customerService.create(any())).thenThrow(new ValidationException(validationErrors));

        mockMvc.perform(post(ControllerTestsConstants.CUSTOMER_PATH_ROOT)
                        .content(TestHelpers.toJsonStr(customerDataDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestHelpers.toJsonStr(new ValidationErrorsDto(validationErrors))));
    }
}
