package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.RegisteredClientAuthenticationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredClientAuthenticationMethodRepository extends JpaRepository<RegisteredClientAuthenticationMethod, RegisteredClientAuthenticationMethod.RegisteredClientAuthenticationMethodId> {

}
