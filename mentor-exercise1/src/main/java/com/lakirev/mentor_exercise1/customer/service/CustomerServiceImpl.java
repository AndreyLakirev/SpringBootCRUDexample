package com.lakirev.mentor_exercise1.customer.service;

import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void insert(List<Customer> customers) {
        for (Customer customer : customers) {
            repository.save(customer);
        }
    }
}
