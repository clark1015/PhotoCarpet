package com.koss.photocarpet.domain.customMood;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CustomMoodTestRepository extends JpaRepository<CustomMood,Long>{
    CustomMood findByCustomMood(String customMood);

}
