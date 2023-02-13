package com.koss.photocarpet.domain.exhibition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Optional<Exhibition> findByExhibitionId(Long exhibitionId);

    List<Exhibition> findAllByOrderByCreateDateDesc();
}
