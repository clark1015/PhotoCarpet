package com.koss.photocarpet.domain.exhibition;

import com.koss.photocarpet.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Exhibition findByExhibitionId(Long exhibitionId);

    List<Exhibition> findAllByOrderByCreateDateDesc();

    List<Exhibition> findByTitleContaining(String title);

    List<Exhibition> findByUser(User user);

    List<Exhibition> findByContentContaining(String content);
}
