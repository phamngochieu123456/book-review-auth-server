package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.RegisteredClientGrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredClientGrantTypeRepository extends JpaRepository<RegisteredClientGrantType, RegisteredClientGrantType.RegisteredClientGrantTypeId> {

}
