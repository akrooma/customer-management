package com.mysite.customers.controller.customer;

import com.mysite.customers.TestHelpers;
import com.mysite.customers.controller.CustomerController;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.service.ICustomerService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test cases for {@link CustomerController#getCustomer(long)}.
 */
@WebMvcTest(CustomerController.class)
public class GetCustomerEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    @Test
    @Order(1)
    void givenKnownCustomerId_whenGetCustomer_thenStatus200AndCustomerDataIsReturned() throws Exception {
        var customerDto = TestHelpers.getValidCustomerDto();

        when(customerService.findById(anyLong())).thenReturn(customerDto);

        var customerDtoJsonStr = TestHelpers.toJsonStr(customerDto);

        mockMvc.perform(get(ControllerTestsConstants.CUSTOMER_PATH_ROOT + ControllerTestsConstants.ID_PATH_VARIABLE, customerDto.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(customerDtoJsonStr));
    }

    @Test
    @Order(2)
    void givenUnknownCustomerId_whenGetCustomer_thenStatus404AndNotFoundMessageIsReturned() throws Exception {
        var unknownCustomerId = -1L;

        when(customerService.findById(anyLong())).thenThrow(new NotFoundException(unknownCustomerId, "Customer"));

        mockMvc.perform(get(ControllerTestsConstants.CUSTOMER_PATH_ROOT + ControllerTestsConstants.ID_PATH_VARIABLE, unknownCustomerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(TestHelpers.getNotFoundExceptionMessage(unknownCustomerId)));
    }
}
