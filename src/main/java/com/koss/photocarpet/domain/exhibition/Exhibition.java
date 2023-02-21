package com.koss.photocarpet.domain.exhibition;
import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.customMood.CustomMood;
import com.koss.photocarpet.domain.likeExhibition.LikeExhibition;
import com.koss.photocarpet.domain.moodRelation.MoodRelation;
import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name="exhibition")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exhibition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long exhibitionId;

    @Column
    private String title;
    private String content;
    private Date exhibitDate;
    private boolean visible;

    private Long likeCount;
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "exhibition", orphanRemoval = true)
    List<Photo> photos = new ArrayList<Photo>();

    @OneToMany(mappedBy = "exhibition", orphanRemoval = true)
    List<LikeExhibition> likeExhibitions = new ArrayList<LikeExhibition>();

    @OneToMany(mappedBy = "exhibition", orphanRemoval = true)
    List<MoodRelation> moodRelations = new ArrayList<MoodRelation>();

    public void updateTitleContentDate(String updateTitle, String updateContent, Date updateDate){
        this.title = updateTitle;
        this.content = updateContent;
        this.exhibitDate = updateDate;
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }





}
