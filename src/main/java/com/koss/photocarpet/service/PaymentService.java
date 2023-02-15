package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.dto.request.PaymentRequest;
import com.koss.photocarpet.controller.dto.response.PaymentResponse;
import com.koss.photocarpet.domain.paymentRecord.PaymentRecord;
import com.koss.photocarpet.domain.paymentRecord.PaymentRepository;
import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.photo.PhotoRepository;
import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PaymentRepository paymentRepository;



    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        User buyer = userRepository.findById(paymentRequest.getBuyerId()).get();
        User seller = userRepository.findById(paymentRequest.getSellerId()).get();
        Photo photo = photoRepository.findByPhotoId(paymentRequest.getPhotoId()).get();

        if(photo != null & !(photo.isSell())) {
            if(buyer.getTotalPoint() >= photo.getPhotoId()){
                PaymentRecord payment =PaymentRecord.builder().buyer(buyer).seller(seller).photo(photo).point(photo.getPrice()).build();
                photo.setSell(true);
                buyer.setTotalPoint(buyer.getTotalPoint() - photo.getPhotoId());
                seller.setTotalPoint(seller.getTotalPoint() + photo.getPrice());
                buyer.getPaymentRecords().add(payment);
                seller.getPaymentRecords().add(payment);
                userRepository.save(buyer);
                userRepository.save(seller);
                photoRepository.save(photo);
                paymentRepository.save(payment);
                return PaymentResponse.builder().buyer(buyer).seller(seller).photo(photo).build();
            }
        }
        else{
            return null;
        }

        return null;
    }
}
