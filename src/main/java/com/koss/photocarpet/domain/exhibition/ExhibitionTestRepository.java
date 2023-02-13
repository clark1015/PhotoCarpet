package com.koss.photocarpet.domain.exhibition;

import com.koss.photocarpet.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhibitionTestRepository extends JpaRepository<Exhibition, Long> {
    Exhibition findByExhibitionId(Long exhibitionId);

    List<Exhibition> findByTitleContaining(String title);

    List<Exhibition> findByUser(User user);

}
