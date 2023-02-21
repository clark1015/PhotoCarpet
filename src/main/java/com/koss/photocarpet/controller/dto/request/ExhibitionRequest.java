package com.koss.photocarpet.controller.dto.request;


import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ExhibitionRequest {
    private Long exhibitionId;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private Date exhibitionDate;
    private Long userId; //일단 userId는 직접받음

    private List<String> customMoods;


    public Exhibition toExhibitionEntity(ExhibitionRequest exhibitionRequest, User user) {
        return Exhibition.builder()
                .exhibitDate(exhibitionRequest.getExhibitionDate())
                .title(exhibitionRequest.getTitle())
                .content(exhibitionRequest.getContent())
                .visible(true)
                .likeCount(0L)
                .user(user).build();
    }
}