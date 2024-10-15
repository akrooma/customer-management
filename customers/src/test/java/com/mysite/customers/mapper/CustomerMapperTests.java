package com.mysite.customers.mapper;

import com.mysite.customers.TestHelpers;
import com.mysite.customers.domain.CustomerFactory;
import com.mysite.customers.dto.mapper.CustomerMapper;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.repository.ICustomerRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class CustomerMapperTests {

    @Autowired
    private ICustomerRepository customerRepository;

    @Test
    @Order(1)
    void givenCustomerEntity_whenToDto_thenDtoValuesMatch() throws ValidationException {
        var validCustomerData = TestHelpers.getValidCustomerData();

        var newCustomer = CustomerFactory.createCustomer(
                validCustomerData.firstName(),
                validCustomerData.lastName(),
                validCustomerData.email());

        newCustomer = customerRepository.save(newCustomer);

        var customerDto = CustomerMapper.toDto(newCustomer);

        assertThat(customerDto).isNotNull();

        assertThat(customerDto.id()).isEqualTo(newCustomer.getId());
        assertThat(customerDto.firstName()).isEqualTo(newCustomer.getFirstName());
        assertThat(customerDto.lastName()).isEqualTo(newCustomer.getLastName());
        assertThat(customerDto.email()).isEqualTo(newCustomer.getEmail());
    }

    @Test
    @Order(2)
    void givenNullCustomerEntity_whenToDto_thenIllegalArgumentExceptionIsThrown() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> CustomerMapper.toDto(null));
    }

}
