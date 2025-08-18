package com.microservice_manager.Security;

import com.microservice_manager.entity.Authority;
import com.microservice_manager.entity.Manager;
import com.microservice_manager.repository.ManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

  private final ManagerRepository managerRepository;

  public DomainUserDetailsService(ManagerRepository managerRepository) {
    this.managerRepository = managerRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(final String email) {
    return managerRepository
        .findByEmail(email.toLowerCase())
        .map(this::createSpringSecurityUser)
        .orElseThrow(() -> new UsernameNotFoundException("El email " + email + " no existe"));
  }

  private UserDetails createSpringSecurityUser(Manager manager) {
    List<Authority> authorities = Collections.singletonList(manager.getAuthority());
    List<GrantedAuthority> grantedAuthorities = authorities
        .stream()
        .map(Authority::getName)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    return new org.springframework.security.core.userdetails.User(manager.getEmail(), manager.getPassword(), grantedAuthorities);
  }

}
