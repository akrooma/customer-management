package com.mysite.customers.service.customer;

import com.mysite.customers.TestHelpers;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.service.ICustomerService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test cases for {@link ICustomerService#findById(Long)}.
 */
@SpringBootTest
public class FindCustomerServiceTests {

    @Autowired
    private ICustomerService customerService;

    @Test
    @Order(1)
    void givenExistingCustomerId_whenFindById_thenCorrectCustomerIsReturned() throws ValidationException, NotFoundException {
        var createdCustomer = customerService.create(TestHelpers.getValidCustomerData());

        assertThat(createdCustomer).isNotNull();

        var queriedCustomer = customerService.findById(createdCustomer.id());

        assertThat(queriedCustomer).isNotNull();

        assertThat(queriedCustomer.id()).isEqualTo(createdCustomer.id());
    }

    /**
     * Also validates the error message.
     */
    @Test
    @Order(2)
    void givenUnknownCustomerId_whenFindById_thenNotFoundExceptionIsThrown() {
        var unknownCustomerId = -1L;

        assertThatExceptionOfType(NotFoundException.class)
               .isThrownBy(() -> customerService.findById(unknownCustomerId))
               .withMessage(TestHelpers.getNotFoundExceptionMessage(unknownCustomerId));
    }
}
