package com.koss.photocarpet.controller.dto.response;


import com.koss.photocarpet.domain.exhibition.Exhibition;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@ToString
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExhibitionResponse {
    private Long exhibitId;
    private String title;
    private String content;
    private Long userId;
    private Long likeCount;
    private Date exhibitionDate;
    private LocalDateTime createDate;
    private String thumbUrl;

    public ExhibitionResponse(Exhibition exhibition) {
        this.exhibitId = exhibition.getExhibitionId();
        this.content = exhibition.getContent();
        this.title = exhibition.getTitle();
        this.userId = exhibition.getUser().getUserId();
        this.likeCount = exhibition.getLikeCount();
        this.exhibitionDate = exhibition.getExhibitDate();
        this.thumbUrl =exhibition.getThumbnailUrl();
        this.createDate = exhibition.getCreateDate();
    }


    public static ExhibitionResponse of(Long exhibitId, String title, String content, Long userId, Long likeCount, Date exhibitionDate,String thumbUrl) {
        return ExhibitionResponse.builder()
                .exhibitId(exhibitId)
                .title(title)
                .content(content)
                .userId(userId)
                .likeCount(likeCount)
                .thumbUrl(thumbUrl)
                .exhibitionDate(exhibitionDate).build();
    }
}

