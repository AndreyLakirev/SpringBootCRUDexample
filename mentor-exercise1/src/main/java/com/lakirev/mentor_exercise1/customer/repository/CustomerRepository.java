package com.lakirev.mentor_exercise1.customer.repository;

import com.lakirev.mentor_exercise1.customer.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
