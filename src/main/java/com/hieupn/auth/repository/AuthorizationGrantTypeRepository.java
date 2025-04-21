package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.AuthorizationGrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationGrantTypeRepository extends JpaRepository<AuthorizationGrantType, Integer> {

}
