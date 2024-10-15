package com.mysite.customers.repository;

import com.mysite.customers.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {
}