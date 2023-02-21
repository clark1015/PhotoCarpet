package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.ImageHandler;
import com.koss.photocarpet.controller.dto.request.PhotoRequest;
import com.koss.photocarpet.controller.dto.response.PhotoResponse;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.photo.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    private final ImageHandler imageHandler;
    private final PhotoRepository photoRepository;
    private final ExhibitionService exhibitionService;
    private final S3Upload s3Upload;
    public void create(PhotoRequest photoRequest, MultipartFile file) throws Exception{
        Exhibition getExhibition = exhibitionService.getExhibition(photoRequest.getExhibitionId());
        invalidArgument(photoRequest);
//        String photoUrl = imageHandler.pareseFileInfo(file,getExhibition.getUser().getUserId());
//        Photo photo = photoRequest.toEntity(photoRequest,getExhibition,photoUrl);
        String S3Url = s3Upload.upload(file);
        Photo photo = photoRequest.toEntity(photoRequest,getExhibition,S3Url);
        photoRepository.save(photo);
    }
    private void invalidArgument(PhotoRequest photoRequest){
        if(photoRequest.getTitle().equals("")||photoRequest.getContent().equals("")){
            log.warn("title : " + photoRequest.getTitle() + ", content : " + photoRequest.getContent());
            throw new IllegalArgumentException("빈 String을 입력하였습니다");
        }
    }

    public void update(PhotoRequest photoRequest, MultipartFile file) throws Exception {
        Photo getPhoto = getPhoto(photoRequest.getPhotoId());
        invalidArgument(photoRequest);
//        imageHandler.deleteFile(getPhoto.getUrl());
//        String photoUrl = imageHandler.pareseFileInfo(file, getPhoto.getExhibition().getUser().getUserId());
        String photoUrl = s3Upload.upload(file);
        getPhoto.updateAll(photoRequest.getTitle(), photoRequest.getContent(), photoUrl, photoRequest.getSell(), photoRequest.getPrice());
        photoRepository.save(getPhoto);
    }
    public Photo getPhoto(Long photoId){
        return photoRepository.findByPhotoId(photoId)
                .orElseThrow(() -> new IllegalArgumentException("없는 전시물 아이디입니다"));
    }

    public void delete(Long photoId) throws Exception{
        Photo getPhoto = getPhoto(photoId);
        imageHandler.deleteFile(getPhoto.getUrl());
        photoRepository.delete(getPhoto);
    }

    public List<PhotoResponse> getArts(Long exhibitionId) {
        Exhibition getExhibition = exhibitionService.getExhibition(exhibitionId);
        List<Photo> allArts = photoRepository.findByExhibition(getExhibition);
        return allArts.stream().map(PhotoResponse::new).collect(Collectors.toList());
    }
}
