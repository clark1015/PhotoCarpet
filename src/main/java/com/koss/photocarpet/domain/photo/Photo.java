package com.koss.photocarpet.domain.photo;
import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.likePhoto.LikePhoto;
import com.koss.photocarpet.domain.paymentRecord.PaymentRecord;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name="photo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long photoId;

    @Column
    private String title;
    private String content;
    private String url;
    private Long price;
    private boolean sell;

    @ManyToOne
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @OneToOne(mappedBy = "photo")
    private PaymentRecord paymentRecord;

    @OneToMany(mappedBy = "photo", orphanRemoval = true)
    List<LikePhoto> likephotos = new ArrayList<LikePhoto>();
    public void updateAll(String title, String content, String photoUrl, Boolean sell, Long price) {
        this.title = title;
        this.content = content;
        this.url = photoUrl;
        this.sell = sell;
        this.price = price;
    }

}
