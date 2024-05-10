package com.clientApi.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmailAndPassword(String email, String password);

    // select * from client where email = ?
    //@Query("SELECT s FROM s WHERE s.email = ?1")
    Optional<Client> findByEmail(String email);
}


