package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.JPARegisteredClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JPARegisteredClientRepository extends JpaRepository<JPARegisteredClient, String> {
    Optional<JPARegisteredClient> findByClientId(String clientID);
}
