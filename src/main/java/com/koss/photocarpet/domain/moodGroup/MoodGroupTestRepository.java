package com.koss.photocarpet.domain.moodGroup;

import com.koss.photocarpet.domain.exhibition.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodGroupTestRepository extends JpaRepository<MoodGroup, Long> {
    List<Exhibition> findByCustomMoodContaining(Optional<?> customMood);
}
