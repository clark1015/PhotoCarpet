package com.koss.photocarpet.domain.likePhoto;
import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name="like_photo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikePhoto extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long likePhotoId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
}