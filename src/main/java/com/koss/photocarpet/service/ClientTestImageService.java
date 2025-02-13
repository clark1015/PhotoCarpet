package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.dto.response.ClientTestResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientTestImageService {
    public List<ClientTestResponse> getImagePaths(){
        List<String> imagePaths = new ArrayList<>();
        List<ClientTestResponse> responses = new ArrayList<>();
        String directoryPath = "src/main/resources/testImages/";
        File directory = new File(directoryPath);
        Long index = 0L;
        for(File file:directory.listFiles()){
            if(file.isFile()){
                String imagePath = directoryPath + file.getName();
                index +=1L;
                responses.add(ClientTestResponse.builder().imageURl(imagePath).index(index).build());
//                imagePaths.add(imagePath);
            }
        }
        return responses;
    }
}
