package com.koss.photocarpet.service;


import com.koss.photocarpet.controller.ImageHandler;
import com.koss.photocarpet.controller.dto.request.ExhibitionRequest;
import com.koss.photocarpet.controller.dto.response.ExhibitionResponse;
import com.koss.photocarpet.controller.dto.response.UserInExhibition;
import com.koss.photocarpet.domain.customMood.CustomMood;
import com.koss.photocarpet.domain.customMood.CustomMoodTestRepository;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.exhibition.ExhibitionRepository;
import com.koss.photocarpet.controller.ImageHandler;
import com.koss.photocarpet.domain.moodRelation.MoodRelation;
import com.koss.photocarpet.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final CustomMoodTestRepository customMoodTestRepository;
    private final ImageHandler imageHandler;
    private final UserService userService;
    private final S3Upload s3Upload;
    //임의의 user 미리 만들어둠.  코드 합칠때 수정예정
    User user = User.builder().userId(1L).nickname("sorr").totalPoint(0L).email("solhee@com").build();
    public void create(ExhibitionRequest exhibitionRequest, MultipartFile file) throws Exception {
        User getUser = userService.getUser(exhibitionRequest.getUserId());
        invalidArguments(exhibitionRequest);
        Exhibition exhibition = exhibitionRequest.toExhibitionEntity(exhibitionRequest,getUser);
//        String exhibitionImageUrl = imageHandler.pareseFileInfo(file,exhibition.getUser().getUserId());
        String exhibitionImageUrl = s3Upload.upload(file);
        exhibition.updateThumbnailUrl(exhibitionImageUrl);
        exhibition = setCustomMoods(exhibitionRequest.getCustomMoods(),exhibition);
        exhibitionRepository.save(exhibition);
    }

    public Exhibition setCustomMoods(List<String> customMoodNames, Exhibition exhibition) {
        if(customMoodNames == null) return exhibition;
        List<MoodRelation> moodRelations = new ArrayList<>();
        for(String customMoodName: customMoodNames) {
            CustomMood customMood = customMoodTestRepository.findByCustomMood(customMoodName);

            if(customMood == null) {
                customMood = CustomMood.builder().customMood(customMoodName).build();
            }
            MoodRelation moodRelation = MoodRelation.builder()
                    .customMood(customMood)
                    .exhibition(exhibition)
                    .build();
            moodRelations.add(moodRelation);
        }
        exhibition.setMoodRelations(moodRelations);
        return exhibition;
    }
    private void invalidArguments(ExhibitionRequest exhibitionRequest){
        if (exhibitionRequest.getTitle().equals("") || exhibitionRequest.getContent().equals("") || exhibitionRequest.getExhibitionDate().equals("")) {
            log.warn("title : " +  exhibitionRequest.getTitle() + ", content : " + exhibitionRequest.getContent() + ", Date : " + exhibitionRequest.getExhibitionDate());
            throw new IllegalArgumentException("빈 string을 입력했습니다");
        }
    }

    public ExhibitionResponse update(ExhibitionRequest exhibitionRequest, MultipartFile file) throws Exception{
        Exhibition getExhibition = getExhibition(exhibitionRequest.getExhibitionId());
        invalidArguments(exhibitionRequest);
//        imageHandler.deleteFile(getExhibition.getThumbnailUrl());
//        s3Upload.fileDelete(getExhibition.getThumbnailUrl());
        String exhibitionImageUrl = s3Upload.upload(file);
        getExhibition.updateTitleContentDate(exhibitionRequest.getTitle(), exhibitionRequest.getContent(),exhibitionRequest.getExhibitionDate());
        getExhibition.updateThumbnailUrl(exhibitionImageUrl);
        Exhibition savedExhibition = exhibitionRepository.save(getExhibition);
        return ExhibitionResponse.of(savedExhibition.getExhibitionId(),savedExhibition.getTitle(),savedExhibition.getContent(),savedExhibition.getUser().getNickname(),savedExhibition.getUser().getProfileUrl(),savedExhibition.getLikeCount(),savedExhibition.getExhibitDate(), savedExhibition.getThumbnailUrl());
    }
    public Exhibition getExhibition(Long exhibitionId){
        Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId);
        if (exhibition ==null) throw new RuntimeException("없는 전시회를 가져올 수 없습니다!");
        return exhibition;
    }

    public void delete(Long exhibitionId) throws Exception{
        Exhibition getExhibition = getExhibition(exhibitionId);
//        imageHandler.deleteFile(getExhibition.getThumbnailUrl());
//        s3Upload.fileDelete(getExhibition.getThumbnailUrl());
        exhibitionRepository.delete( getExhibition);
    }

    public void saveImage(MultipartFile file,Long exhibitionId) throws Exception {
        Exhibition getExhibiton = getExhibition(exhibitionId);
//        String url = imageHandler.pareseFileInfo(files,getExhibiton.getUser().getUserId());
        String url =  s3Upload.upload(file);
        getExhibiton.updateThumbnailUrl(url);
        exhibitionRepository.save(getExhibiton);
    }

    public List<ExhibitionResponse> morelike() {
        Sort likeRecent = Sort.by(
                Sort.Order.desc("likeCount"),
                Sort.Order.desc("createDate")
        );
        List<Exhibition> arrayLikeToRecent = exhibitionRepository.findAll(likeRecent);
        return arrayLikeToRecent.stream().map(ExhibitionResponse::new).collect(Collectors.toList());
    }
    public List<ExhibitionResponse> recent(){
        List<Exhibition> recentExhibitions = exhibitionRepository.findAllByOrderByCreateDateDesc();
        return recentExhibitions.stream().map(ExhibitionResponse::new).collect(Collectors.toList());

    }
    public void uploadLocalImage(ExhibitionRequest exhibitionRequest, MultipartFile file) throws Exception {
        User getUser = userService.getUser(exhibitionRequest.getUserId());
        invalidArguments(exhibitionRequest);
        Exhibition exhibition = exhibitionRequest.toExhibitionEntity(exhibitionRequest,getUser);
        String exhibitionImageUrl = imageHandler.pareseFileInfo(file,exhibition.getUser().getUserId());
        exhibition.updateThumbnailUrl(exhibitionImageUrl);
        exhibitionRepository.save(exhibition);
    }

    public ExhibitionResponse findByID(Long exhibitionId) {
        Exhibition getExhibition = getExhibition(exhibitionId);
        return ExhibitionResponse.of(getExhibition.getExhibitionId(),getExhibition.getTitle(), getExhibition.getContent(), getExhibition.getUser().getNickname(),getExhibition.getUser().getProfileUrl(), getExhibition.getLikeCount(),getExhibition.getExhibitDate(), getExhibition.getThumbnailUrl());

    }
}
