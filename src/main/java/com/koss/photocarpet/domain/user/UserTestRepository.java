package com.koss.photocarpet.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTestRepository extends JpaRepository<User, Long> {
//    User findByUserId(Long userId);
    Optional<User> findByUserId(Long userId);
    List<User> findByNickname(String nickname);
}
