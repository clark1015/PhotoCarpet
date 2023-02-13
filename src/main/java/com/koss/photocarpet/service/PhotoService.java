package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.ImageHandler;
import com.koss.photocarpet.controller.dto.request.PhotoRequest;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.photo.Photo;
import com.koss.photocarpet.domain.photo.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoService {
    private final ImageHandler imageHandler;
    private final PhotoRepository photoRepository;
    private final ExhibitionService exhibitionService;
    public void create(PhotoRequest photoRequest, MultipartFile file) throws Exception{
        Exhibition getExhibition = exhibitionService.getExhibition(photoRequest.getExhibitionId());
        invalidArgument(photoRequest);
        String photoUrl = imageHandler.pareseFileInfo(file,getExhibition.getUser().getUserId());
        Photo photo = photoRequest.toEntity(photoRequest,getExhibition,photoUrl);
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
        imageHandler.deleteFile(getPhoto.getUrl());
        String photoUrl = imageHandler.pareseFileInfo(file, getPhoto.getExhibition().getUser().getUserId());
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
}
