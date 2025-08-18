package com.microservice_voluntary.repository;

import com.microservice_voluntary.dto.VoluntaryDTO;
import com.microservice_voluntary.entity.Voluntary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoluntaryRepository extends JpaRepository<Voluntary, Long> {

  @Query("SELECT v FROM Voluntary v WHERE v.email = :email")
  Optional<VoluntaryDTO> findByEmail(@Param("email") String email);
}
