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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test cases for {@link CustomerController#deleteCustomer(long)}.
 */
@WebMvcTest(CustomerController.class)
public class DeleteCustomerEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    @Test
    @Order(1)
    void givenKnownCustomerId_whenDeleteCustomer_thenStatus200AndNoContentIsReturned() throws Exception {
        mockMvc.perform(delete(ControllerTestsConstants.CUSTOMER_PATH_ROOT + ControllerTestsConstants.ID_PATH_VARIABLE, 1L))
               .andExpect(status().isNoContent())
               .andExpect(content().bytes(new byte[0]));
    }

    @Test
    @Order(2)
    void givenUnknownCustomerId_whenGetCustomerById_thenStatus404AndNotFoundMessageIsReturned() throws Exception {
        var unknownCustomerId = -1L;

        doThrow(new NotFoundException(unknownCustomerId, "Customer")).when(customerService).deleteById(anyLong());

        mockMvc.perform(delete(ControllerTestsConstants.CUSTOMER_PATH_ROOT + ControllerTestsConstants.ID_PATH_VARIABLE, unknownCustomerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN))
                .andExpect(content().string(TestHelpers.getNotFoundExceptionMessage(unknownCustomerId)));
    }
}
