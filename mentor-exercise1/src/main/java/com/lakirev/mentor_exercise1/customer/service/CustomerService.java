package com.lakirev.mentor_exercise1.customer.service;

import com.lakirev.mentor_exercise1.customer.model.Customer;

import java.util.List;

public interface CustomerService {
    void insert(Customer customer);

    void insert(List<Customer> customers);
}
