package com.koss.photocarpet.controller;

import com.koss.photocarpet.controller.dto.request.PaymentRequest;
import com.koss.photocarpet.controller.dto.response.PaymentResponse;
import com.koss.photocarpet.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
// 사는 사람 입장에서 쓰는 컨트롤러 -> 사는 사람의 돈이 충분한지, 사진이 아직 안팔렸는지, 파는 사람이 유효한 유저인지 확인.
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    public PaymentResponse create(@RequestBody PaymentRequest paymentRequest){
        return paymentService.createPayment(paymentRequest);
    }

}
