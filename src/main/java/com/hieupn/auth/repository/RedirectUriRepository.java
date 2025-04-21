package com.hieupn.auth.repository;

import com.hieupn.auth.model.entity.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedirectUriRepository extends JpaRepository<RedirectUri, Integer> {

}
