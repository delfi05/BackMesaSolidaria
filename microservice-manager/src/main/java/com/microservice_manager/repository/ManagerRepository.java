package com.microservice_manager.repository;

import com.microservice_manager.entity.Manager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

  @Query("SELECT m FROM Manager m WHERE m.email = :email")
  Optional<Manager> findByEmail(@Param("email") String email);

  @Modifying
  @Transactional
  @Query(value = "UPDATE Manager m SET " +
      "name = COALESCE(:name, name), " +
      "last_name = COALESCE(:lastName, last_name), " +
      "email = COALESCE(:email, email), " +
      "password = COALESCE(:password, password) " +
      "WHERE id_manager = :id", nativeQuery = true)
  void updateManager(@Param("id") Long id,
                     @Param("name") String name,
                     @Param("lastName") String lastName,
                     @Param("email") String email,
                     @Param("password") String password);

  @Modifying
  @Transactional
  @Query("UPDATE Manager m SET m.password = :password WHERE m.email = :email")
  void updateManagerPassword(@Param("email") String email, @Param("password") String password);
}
