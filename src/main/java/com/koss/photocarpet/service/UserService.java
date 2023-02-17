package com.koss.photocarpet.service;

import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getUser(Long userId) {
        User getUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 user가 없습니다"));
        return getUser;
    }
}
