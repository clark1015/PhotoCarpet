package com.koss.photocarpet.controller.dto.request;

import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.likeExhibition.LikeExhibition;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeExhibitionRequest {
    private Long likeExhibitionId;
    private User user;
    private Exhibition exhibition;


    public static LikeExhibition toEntity(LikeExhibitionRequest likeExhibitionRequest) {
        return LikeExhibition.builder()
                .likeExhibitionId(likeExhibitionRequest.getLikeExhibitionId())
                .user(likeExhibitionRequest.getUser())
                .exhibition(likeExhibitionRequest.getExhibition())
                .build();
    }
}
