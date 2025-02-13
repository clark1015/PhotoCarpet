package com.koss.photocarpet.controller;

import com.koss.photocarpet.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {
    // userId
    @PostMapping
    public String chargePoint(){
            return null;
    }
}
