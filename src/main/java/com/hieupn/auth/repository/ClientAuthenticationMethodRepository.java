package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.ClientAuthenticationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAuthenticationMethodRepository extends JpaRepository<ClientAuthenticationMethod, Integer> {

}
