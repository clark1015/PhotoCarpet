package com.koss.photocarpet.domain.user;

import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.customMood.CustomMood;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.likeExhibition.LikeExhibition;
import com.koss.photocarpet.domain.likePhoto.LikePhoto;
import com.koss.photocarpet.domain.paymentRecord.PaymentRecord;
import com.koss.photocarpet.domain.pointRecord.PointRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name="user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column
    private String nickname;
    private Long totalPoint;
    private String email;
    private String profileUrl;


    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<LikePhoto> likePhotos = new ArrayList<LikePhoto>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<LikeExhibition> likeExhibitions = new ArrayList<LikeExhibition>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Exhibition> exhibitions = new ArrayList<Exhibition>();

    @OneToMany(mappedBy = "buyer", orphanRemoval = true)
    List<PaymentRecord> paymentRecords = new ArrayList<PaymentRecord>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<PointRecord> pointRecords = new ArrayList<PointRecord>();

}
