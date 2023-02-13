package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.dto.request.LikeExhibitionRequest;
import com.koss.photocarpet.domain.exhibition.ExhibitionTestRepository;
import com.koss.photocarpet.domain.likeExhibition.LikeExhibition;
import com.koss.photocarpet.domain.likeExhibition.LikeExhibitionTestRepository;
import com.koss.photocarpet.domain.user.UserTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeExhibitionService {
    @Autowired
    private ExhibitionTestRepository exhibitionTestRepository;
    @Autowired
    private UserTestRepository userTestRepository;
    @Autowired
    private LikeExhibitionTestRepository likeExhibitionTestRepository;
    public void push_like(final Long userId, final Long exhibitionId, final LikeExhibitionRequest likeExhibitionRequest) {
        LikeExhibition likeExhibition = likeExhibitionRequest.toEntity(likeExhibitionRequest);
        likeExhibition.setUser(userTestRepository.findByUserId(userId));
        likeExhibition.setExhibition(exhibitionTestRepository.findByExhibitionId(exhibitionId));

        likeExhibitionTestRepository.save(likeExhibition);
    }
}
