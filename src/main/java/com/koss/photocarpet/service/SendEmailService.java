package com.koss.photocarpet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import com.sendgrid.*;

@Service
@Slf4j
public class SendEmailService {
    public void sendEmail(String recipientEmail, String subject, String content) throws IOException {
        System.out.println("하나");
        Email from = new Email("seok626898@gmail.com"); // 발신자 이메일 주소
        Email to = new Email(recipientEmail); // 수신자 이메일 주소
        System.out.println("수신 이메일ㅣ " +recipientEmail);
        Content messageContent = new Content("text/plain", content); // 이메일 내용
        Mail mail = new Mail(from, subject, to, messageContent);

        String apiKey = "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";
        System.out.println("인증 key: " + apiKey);

        SendGrid sg = new SendGrid(apiKey); // SendGrid API 키
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println(request);
            System.out.println(response.getStatusCode()); // 이메일 전송 성공 시 202 반환
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }catch (IOException ex) {
            throw ex;
        }
    }
}
