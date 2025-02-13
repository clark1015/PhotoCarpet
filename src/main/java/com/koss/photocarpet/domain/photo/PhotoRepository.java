package com.koss.photocarpet.domain.photo;

import com.koss.photocarpet.domain.exhibition.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    Optional<Photo> findByPhotoId(Long photoId);

    List<Photo> findByExhibition(Exhibition exhibition);
}
