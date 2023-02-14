package com.koss.photocarpet.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ImageHandler {
    public String pareseFileInfo(MultipartFile multipartFiles, Long userId) throws Exception{
        // 파일이 빈 것이 들어오면 빈 것을 반환
        if (multipartFiles.isEmpty()) {
            return "em";
        }
        // 파일 이름을 업로드 한 날짜로 바꾸어서 저장할 것이다
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String current_date = simpleDateFormat.format(new Date());

        // 프로젝트 폴더에 저장하기 위해 절대경로를 설정 (Window 의 Tomcat 은 Temp 파일을 이용한다)
        String absolutePath = new File("").getAbsolutePath() + "/";

        // 경로를 지정하고 그곳에다가 저장
        String path = "images/" + current_date;
        File file = new File(path);
        // 저장할 위치의 디렉토리가 존지하지 않을 경우
        if (!file.exists()) {
            // mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
            file.mkdirs();
        }
        String contentType = multipartFiles.getContentType();
        String originalFileExtension = "";
        // 확장자 명이 없으면 이 파일은 잘 못 된 것이다
            if (contentType.contains("image/jpeg")) {
                originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
                originalFileExtension = ".png";
            } else if (contentType.contains("image/gif")) {
                originalFileExtension = ".gif";
            }
        // 각 이름은 겹치면 안되므로 나노 초까지 동원하여 지정
        String new_file_name = userId + "_"+ System.nanoTime() + originalFileExtension;

        file = new File(absolutePath + path + "/" + new_file_name);
        multipartFiles.transferTo(file);
        return  path + "/" + new_file_name;
    }
    public String deleteFile(String imageUrl) throws Exception{
        String filePath = new File("").getAbsolutePath() + "/";
        File deleteFile = new File(filePath + imageUrl);
        if(deleteFile.exists()){
            deleteFile.delete();
            return "ok";
        }
        throw new Exception("존재하지 않는 파일입니다");

    }
}
