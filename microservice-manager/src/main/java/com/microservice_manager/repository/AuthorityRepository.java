package com.microservice_manager.repository;


import com.microservice_manager.dto.AuthorityDTO;
import com.microservice_manager.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

  @Query("SELECT a FROM Authority a WHERE a.name = :name")
  AuthorityDTO getAuthorityByName(@Param("name") String name);
}
