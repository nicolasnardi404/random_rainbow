package com.randomrainbow.springboot.demosecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.randomrainbow.springboot.demosecurity.entity.User;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(long idUser);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByRefreshToken(String token);

}
