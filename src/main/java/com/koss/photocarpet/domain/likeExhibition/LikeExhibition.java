package com.koss.photocarpet.domain.likeExhibition;
import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name="like_exhibition")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeExhibition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long likeExhibitionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
}
