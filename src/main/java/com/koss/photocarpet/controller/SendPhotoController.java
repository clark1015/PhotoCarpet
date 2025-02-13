package com.koss.photocarpet.controller;

import com.koss.photocarpet.service.EmailService;
import com.koss.photocarpet.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sendPhoto")
@RequiredArgsConstructor
@Slf4j
public class SendPhotoController {
    @Autowired
    private EmailService emailService;

    @GetMapping
    public void sendPhoto(@RequestPart String email, @RequestPart String photoUrl) {
        String subject = "테스트 사진이 도착했습니다.";
        String content = "<h1>구매하신 사진 url:</h1> " + photoUrl;


        try {
            emailService.sendMessage(email, subject, content);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }
}
