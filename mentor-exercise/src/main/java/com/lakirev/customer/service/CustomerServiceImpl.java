package com.lakirev.customer.service;

import com.lakirev.customer.model.Customer;
import com.lakirev.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void insert(Customer customer) {
        repository.save(customer);
    }
}
