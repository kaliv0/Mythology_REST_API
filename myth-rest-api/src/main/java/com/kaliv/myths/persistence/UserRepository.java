package com.kaliv.myths.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
