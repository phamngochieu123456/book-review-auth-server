package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.RegisteredClientScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredClientScopeRepository extends JpaRepository<RegisteredClientScope, RegisteredClientScope.RegisteredClientScopeId> {

}
