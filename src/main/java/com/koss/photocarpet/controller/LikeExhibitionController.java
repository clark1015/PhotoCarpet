package com.koss.photocarpet.controller;

import com.koss.photocarpet.controller.dto.request.LikeExhibitionRequest;
import com.koss.photocarpet.service.LikeExhibitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@Controller
public class LikeExhibitionController {

    @Autowired
    private LikeExhibitionService likeExhibitionService;
    @PostMapping("/like")
    public ResponseEntity<?> like(@AuthenticationPrincipal Long userId, @RequestParam Long exhibitionId ,@RequestBody LikeExhibitionRequest likeExhibitionRequest) {
        likeExhibitionService.push_like(userId, exhibitionId, likeExhibitionRequest);
        return ResponseEntity.ok("좋아요 누르기 완료");
    }

    @DeleteMapping("/dislike")
    public ResponseEntity<?> dislike(@AuthenticationPrincipal Long userId, @RequestParam Long exhibitionId) {
        likeExhibitionService.delete_like(userId, exhibitionId);
        return ResponseEntity.ok("좋아요 삭제 완료");
    }

}
