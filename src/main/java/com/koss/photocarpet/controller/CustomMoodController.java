package com.koss.photocarpet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CustomMoodController {
//    @PostMapping("/{exhibition_id}")
//    public ResponseEntity<?> addCustomMood(@PathVariable Long exhibition_id, @AuthenticationPrincipal String) {
//        return ResponseEntity<?>
//    }
@GetMapping("/testing")
public String test(){
    return "test succeed";
}
}
