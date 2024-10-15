package com.mysite.customers.domain;

import com.mysite.customers.TestHelpers;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.repository.ICustomerRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Purpose of these tests is to make sure that the CustomerListener @PrePersist and @PreUpdate handlers work as expected.
 */
@SpringBootTest
public class CustomerListenerTests {
    @Autowired
    private ICustomerRepository customerRepository;

    @Test
    @Order(1)
    void beforeInsert_setsCreatedAndModifiedDates() throws ValidationException {
        var customer = createCustomer();

        var createdDTime = customer.getCreatedDtime();
        var modifiedDTime = customer.getModifiedDtime();

        assertThat(createdDTime).isNotNull();
        assertThat(modifiedDTime).isNotNull();
        assertEquals(createdDTime, modifiedDTime);
    }

    @Test
    @Order(2)
    void beforeUpdate_setsNewModifiedDate() throws ValidationException {
        var customer = customerRepository.findById(1L).orElse(null);

        // In case this test was run in "stand-alone" mode -- we cannot reuse the customer from previous test.
        if (customer == null) {
            customer = createCustomer();
        }

        customer.update(customer.getFirstName(), customer.getLastName(), "newemail@domain.com");

        customer = customerRepository.save(customer);

        var modifiedDTime = customer.getModifiedDtime();
        var createdDTime = customer.getCreatedDtime();

        assertThat(modifiedDTime).isAfter(createdDTime);
    }

    private Customer createCustomer() throws ValidationException {
        var validCustomerData = TestHelpers.getValidCustomerData();

        var customer = CustomerFactory.createCustomer(
                validCustomerData.firstName(),
                validCustomerData.lastName(),
                validCustomerData.email());

        customerRepository.save(customer);

        return customer;
    }
}
