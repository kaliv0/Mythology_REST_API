package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
