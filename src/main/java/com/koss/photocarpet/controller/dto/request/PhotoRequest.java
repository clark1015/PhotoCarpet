package com.koss.photocarpet.controller.dto.request;

import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.photo.Photo;
import lombok.*;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PhotoRequest {
    private Long photoId;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private Long exhibitionId;
    @NotNull
    private Boolean sell;
    private Long price;

    public Photo toEntity(PhotoRequest photoRequest, Exhibition getExhibition, String photoUrl) {
        return Photo.builder()
                .title(photoRequest.getTitle())
                .content(photoRequest.getContent())
                .exhibition(getExhibition)
                .sell(photoRequest.getSell())
                .price(photoRequest.getPrice())
                .url(photoUrl)
                .build();
    }
}
