package com.koss.photocarpet.controller;

import com.koss.photocarpet.controller.dto.request.ExhibitionRequest;
import com.koss.photocarpet.controller.dto.response.ExhibitionResponse;
import com.koss.photocarpet.service.ExhibitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController {
    private final ExhibitionService exhibitionService;
    @PostMapping(value = "/create",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> create ( @RequestPart ExhibitionRequest exhibitionRequest,@RequestPart MultipartFile file) throws Exception{
        exhibitionService.create(exhibitionRequest,file);
        return ResponseEntity.ok("ok");
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(final @RequestBody ExhibitionRequest exhibitionRequest) throws Exception{
        ExhibitionResponse updateExhibition = exhibitionService.update(exhibitionRequest);
        return ResponseEntity.ok(updateExhibition);
    }
    @DeleteMapping("/{exhibitionId}")
    public String delete(@PathVariable Long exhibitionId) throws Exception{
        exhibitionService.delete(exhibitionId);
        return "delete : " + exhibitionId;
    }
    @PutMapping(value = "/image")
    public ResponseEntity<?> saveImage(@RequestParam MultipartFile files,@RequestParam Long exhibitionId) throws Exception{
        exhibitionService.saveImage(files, exhibitionId);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/morelike")
    public ResponseEntity<?> morelike(){
        List<ExhibitionResponse> likeToRecentexhibitions = exhibitionService.morelike();
        return ResponseEntity.ok(likeToRecentexhibitions);
    }
    @GetMapping("/recent")
    public ResponseEntity<?> recent(){
        List<ExhibitionResponse> recentExhibitions = exhibitionService.recent();
        return ResponseEntity.ok(recentExhibitions);
    }

}
