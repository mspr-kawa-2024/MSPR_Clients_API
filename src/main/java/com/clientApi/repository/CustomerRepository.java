package com.clientApi.repository;

import com.clientApi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmailAndPassword(String email, String password);

    // select * from client where email = ?
    //@Query("SELECT s FROM s WHERE s.email = ?1")
    Optional<Customer> findByEmail(String email);
}