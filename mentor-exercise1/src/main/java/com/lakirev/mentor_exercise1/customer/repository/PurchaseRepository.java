package com.lakirev.mentor_exercise1.customer.repository;

import com.lakirev.mentor_exercise1.customer.model.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
}
