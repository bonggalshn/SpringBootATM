package com.bank.repository;

import com.bank.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Customer, String> {
    Customer findByAccountNumber(String accountNumber);
    Customer save(Customer customer);
}
