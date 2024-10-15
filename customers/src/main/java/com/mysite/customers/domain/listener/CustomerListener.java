package com.mysite.customers.domain.listener;

import com.mysite.customers.domain.Customer;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class CustomerListener {

    @PrePersist
    public void beforeInsert(Customer customer) {
        var dateNow = new Date();

        customer.setCreatedDtime(dateNow);
        customer.setModifiedDtime(dateNow);
    }

    @PreUpdate
    public void beforeUpdate(Customer customer) {
        customer.setModifiedDtime(new Date());
    }

}
