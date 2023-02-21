package com.koss.photocarpet.domain.photo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    Optional<Photo> findByPhotoId(Long photoId);
}
