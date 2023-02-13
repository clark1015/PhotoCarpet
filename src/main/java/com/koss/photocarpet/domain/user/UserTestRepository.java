package com.koss.photocarpet.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTestRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);
}
