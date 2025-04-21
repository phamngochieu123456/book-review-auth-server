package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScopeRepository extends JpaRepository<Scope, Integer> {

}
