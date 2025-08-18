package com.microservice_manager.repository;

import com.microservice_manager.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, String> {

  @Transactional
  @Modifying
  @Query("DELETE FROM BlacklistToken bt WHERE bt.createdAt < :expirationTime")
  void deleteExpiredTokens(LocalDateTime expirationTime);
}
