package com.koss.photocarpet.domain.moodRelation;

import com.koss.photocarpet.domain.customMood.CustomMood;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodRelationTestRepository extends JpaRepository<MoodRelation, Long> {
    List<Exhibition> findByCustomMoodContaining(CustomMood customMood);
}
