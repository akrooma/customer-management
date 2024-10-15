package com.mysite.customers.service.impl;

import com.mysite.customers.domain.Customer;
import com.mysite.customers.domain.CustomerFactory;
import com.mysite.customers.dto.CustomerDataDto;
import com.mysite.customers.dto.CustomerDto;
import com.mysite.customers.dto.mapper.CustomerMapper;
import com.mysite.customers.exception.NotFoundException;
import com.mysite.customers.exception.ValidationException;
import com.mysite.customers.repository.ICustomerRepository;
import com.mysite.customers.service.ICustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class CustomerServiceImpl implements ICustomerService {
    private final ICustomerRepository customerRepository;

    public CustomerServiceImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDto findById(long id) throws NotFoundException {
        var customer = getCustomer(id);

        return CustomerMapper.toDto(customer);
    }

    @Override
    public CustomerDto create(CustomerDataDto newCustomerDataDto) throws IllegalArgumentException, ValidationException {
        Assert.notNull(newCustomerDataDto, "New customer data DTO cannot be null");

        var newCustomer = CustomerFactory.createCustomer(
                newCustomerDataDto.firstName(),
                newCustomerDataDto.lastName(),
                newCustomerDataDto.email());

        newCustomer = customerRepository.save(newCustomer);

        return CustomerMapper.toDto(newCustomer);
    }

    @Transactional
    @Override
    public CustomerDto update(long id, CustomerDataDto updatedCustomerDataDto) throws IllegalArgumentException, NotFoundException, ValidationException {
        Assert.notNull(updatedCustomerDataDto, "Updated customer data DTO cannot be null");

        var customer = getCustomer(id);

        customer.update(
                updatedCustomerDataDto.firstName(),
                updatedCustomerDataDto.lastName(),
                updatedCustomerDataDto.email());

        customer = customerRepository.save(customer);

        return CustomerMapper.toDto(customer);
    }

    @Override
    public void deleteById(long id) throws NotFoundException {
        var customer = getCustomer(id);

        customerRepository.delete(customer);
    }

    private Customer getCustomer(long id) throws NotFoundException {
        var customer = customerRepository.findById(id).orElse(null);

        if (customer == null) throw new NotFoundException(id, "Customer");

        return customer;
    }
}
