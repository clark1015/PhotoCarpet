package com.koss.photocarpet.domain.likeExhibition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeExhibitionTestRepository extends JpaRepository<LikeExhibition,Long> {

    LikeExhibition findByLikeExhibitionId(Long likeExhibitionId);
}
