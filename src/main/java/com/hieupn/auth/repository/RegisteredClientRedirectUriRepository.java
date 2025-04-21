package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.RegisteredClientRedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredClientRedirectUriRepository extends JpaRepository<RegisteredClientRedirectUri, RegisteredClientRedirectUri.RegisteredClientRedirectUriId> {

}
