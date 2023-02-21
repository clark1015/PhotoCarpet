package com.koss.photocarpet.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
//, MultipartFile file
    public void sendMessage(String toEmail, String title, String contentHtml) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        try {
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(contentHtml,true);

            //이미지 파일 첨부
//            FileSystemResource imgFile = new FileSystemResource(new File(imgPath));
//            helper.addInline("img", imgFile);

            javaMailSender.send(message);
        }catch (Exception e) {
            log.info(String.valueOf(e));
        }
    }

    public void fileHandle(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        File destination = new File("upload/dir" + originalFileName);
        try {
            file.transferTo(destination);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
