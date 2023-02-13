package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.dto.request.ExhibitionRequest;
import com.koss.photocarpet.controller.dto.response.ExhibitionResponse;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.exhibition.ExhibitionRepository;
import com.koss.photocarpet.controller.ImageHandler;
import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserTestRepository;
import com.sun.xml.bind.v2.schemagen.xmlschema.NestedParticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final UserTestRepository userTestRepository;
    private final ImageHandler imageHandler;
    //임의의 user 미리 만들어둠.  코드 합칠때 수정예정
    User user = User.builder().userId(1L).nickname("sorr").totalPoint(0L).email("solhee@com").build();
    public void create(ExhibitionRequest exhibitionRequest, MultipartFile file) throws Exception {
//        userTestRepository.save(user);
        invalidArguments(exhibitionRequest);
        Exhibition exhibition = exhibitionRequest.toExhibitionEntity(exhibitionRequest,user);
        String exhibitionImageUrl = imageHandler.pareseFileInfo(file,exhibition.getUser().getUserId());
        exhibition.updateThumbnailUrl(exhibitionImageUrl);
        exhibitionRepository.save(exhibition);
    }
    private void invalidArguments(ExhibitionRequest exhibitionRequest){
        if (exhibitionRequest.getTitle().equals("") || exhibitionRequest.getContent().equals("") || exhibitionRequest.getExhibitionDate().equals("")) {
            log.warn("title : " +  exhibitionRequest.getTitle() + ", content : " + exhibitionRequest.getContent() + ", Date : " + exhibitionRequest.getExhibitionDate());
            throw new IllegalArgumentException("빈 string을 입력했습니다");
        }
    }

    public ExhibitionResponse update(ExhibitionRequest exhibitionRequest) throws Exception{
        Exhibition getExhibition = getExhibition(exhibitionRequest.getExhibitionId());
        invalidArguments(exhibitionRequest);
        imageHandler.deleteFile(getExhibition.getThumbnailUrl());
        getExhibition.updateTitleContentDate(exhibitionRequest.getTitle(), exhibitionRequest.getContent(),exhibitionRequest.getExhibitionDate());
        Exhibition savedExhibition = exhibitionRepository.save(getExhibition);
        return ExhibitionResponse.of(savedExhibition.getExhibitionId(),savedExhibition.getTitle(),savedExhibition.getContent(),savedExhibition.getUser().getUserId(),savedExhibition.getLikeCount(),savedExhibition.getExhibitDate());
    }
    public Exhibition getExhibition(Long exhibitionId){
        Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibitionId)
                .orElseThrow(() -> new IllegalArgumentException("없는 전시회 아이디입니다"));
        return exhibition;
    }

    public void delete(Long exhibitionId) throws Exception{
        Exhibition getExhibition = getExhibition(exhibitionId);
        exhibitionRepository.delete( getExhibition);
        imageHandler.deleteFile(getExhibition.getThumbnailUrl());
    }

    public void saveImage(MultipartFile files,Long exhibitionId) throws Exception {
        Exhibition getExhibiton = getExhibition(exhibitionId);
        String url = imageHandler.pareseFileInfo(files,getExhibiton.getUser().getUserId());
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
}
