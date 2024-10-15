package com.mysite.customers.service.customer;

import com.mysite.customers.TestHelpers;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.repository.ICustomerRepository;
import com.mysite.customers.service.ICustomerService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test cases for {@link ICustomerService#deleteById(Long)}.
 */
@SpringBootTest
public class DeleteCustomerServiceTests {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICustomerRepository customerRepository;

    @Test
    @Order(1)
    void givenExistingCustomerId_whenDeleteById_thenCustomerIsRemovedFromDb() throws ValidationException, NotFoundException {
        var customer = customerService.create(TestHelpers.getValidCustomerData());

        assertThat(customer).isNotNull();

        customerService.deleteById(customer.id());

        var deletedCustomer = customerRepository.findById(customer.id()).orElse(null);

        assertThat(deletedCustomer).isNull();
    }

    @Test
    @Order(2)
    void givenUnknownCustomerId_whenDeleteById_thenNotFoundExceptionIsThrown() {
        var unknownCustomerId = -1L;

        assertThatThrownBy(() -> customerService.deleteById(unknownCustomerId))
               .isInstanceOf(NotFoundException.class)
               .hasMessage(TestHelpers.getNotFoundExceptionMessage(unknownCustomerId));
    }
}
