package com.lakirev.mentor_exercise1.customer.repository;

import com.lakirev.mentor_exercise1.customer.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
}
