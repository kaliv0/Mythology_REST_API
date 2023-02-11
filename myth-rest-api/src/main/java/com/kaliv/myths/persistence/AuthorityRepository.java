package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.entity.users.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
