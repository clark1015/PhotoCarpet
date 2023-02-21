package com.koss.photocarpet.controller.dto.response;

import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.photo.Photo;
import lombok.*;

@ToString
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoResponse {
    private Long exhibitionId;
    private Long photoId;
    private String title;
    private String content;
    private String artUrl;
    private Long price;
    private boolean soldOut;
    public PhotoResponse(Photo photo){
        this.exhibitionId = photo.getExhibition().getExhibitionId();
        this.photoId = photo.getPhotoId();
        this.content = photo.getContent();
        this.title = photo.getTitle();
        this.artUrl = photo.getUrl();
        this.price = photo.getPrice();
        this.soldOut = photo.isSell();
    }
    public static PhotoResponse of(Long exhibitionId, Long photoId, String title, String content, String artUrl, Long price, boolean soldOut) {
        return PhotoResponse.builder()
                .exhibitionId(exhibitionId)
                .photoId(photoId)
                .title(title)
                .content(content)
                .artUrl(artUrl)
                .price(price)
                .soldOut(soldOut).build();
    }
}
