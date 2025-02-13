package com.koss.photocarpet.controller;

import com.koss.photocarpet.controller.dto.response.ClientTestResponse;
import com.koss.photocarpet.service.ClientTestImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class ClientTestController {
    private final ClientTestImageService clientTestImageService;

    @GetMapping("/getallimages")
    public ResponseEntity<?> getAllImages() {
        List<ClientTestResponse> responses = clientTestImageService.getImagePaths();
        return ResponseEntity.ok(responses);
    }
}
