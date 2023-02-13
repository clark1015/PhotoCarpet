package com.koss.photocarpet.domain.paymentRecord;

import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name="payment_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecord extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recordId;

    @Column
    private Long point;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne
    private Photo photo;


}
