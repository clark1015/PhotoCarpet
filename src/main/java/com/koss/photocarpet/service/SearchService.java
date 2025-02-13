package com.koss.photocarpet.service;

import com.koss.photocarpet.controller.dto.response.ExhibitionResponse;
import com.koss.photocarpet.domain.customMood.CustomMood;
import com.koss.photocarpet.domain.customMood.CustomMoodTestRepository;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.exhibition.ExhibitionRepository;
import com.koss.photocarpet.domain.moodRelation.MoodRelationTestRepository;
import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class SearchService {
    @Autowired
    private CustomMoodTestRepository customMoodTestRepository;
    @Autowired
    private ExhibitionRepository exhibitionRepository;
    @Autowired
    private MoodRelationTestRepository moodRelationTestRepository;
    @Autowired
    private UserTestRepository userTestRepository;

    Map<Long, Integer> search_score_record;
    public LinkedHashSet<ExhibitionResponse> search(String keyword) {
        search_score_record = new HashMap<Long, Integer>();
        Pattern pattern = Pattern.compile("\\s+");

        String[] splitKeywordBySpace = pattern.split(keyword);
        List<String> splitKeywordBySpaceList = Arrays.asList(splitKeywordBySpace);

        for(String splitKeyword: splitKeywordBySpaceList) {
            searchByCustomMood(splitKeyword);
            searchByExhibitionName(splitKeyword);
            searchByUsername(splitKeyword);
            searchByExhibitionContent(splitKeyword);
        }

        LinkedHashSet<Long> searchResult = new LinkedHashSet<>();

        List<Long> keySet = new ArrayList<>(search_score_record.keySet());
        keySet.sort((o1,o2) -> search_score_record.get(o2).compareTo(search_score_record.get(o1)));

        System.out.println(search_score_record);

        return setResult(keySet);
    }

    private LinkedHashSet<ExhibitionResponse> setResult(List<Long> keySet) {
        LinkedHashSet<ExhibitionResponse> result = new LinkedHashSet<>();
        for (Long exhibition_id :  keySet) {
            Exhibition exhibition = exhibitionRepository.findByExhibitionId(exhibition_id);
            ExhibitionResponse exhibitionResponse = ExhibitionResponse.builder()
                    .exhibitId(exhibition.getExhibitionId())
                    .title(exhibition.getTitle())
                    .content(exhibition.getContent())
                    .exhibitionDate(exhibition.getExhibitDate())
                    .build();
            result.add(exhibitionResponse);
        }
        return result;
    }

    private String searchByCustomMood(String keyword) {
        CustomMood customMood = customMoodTestRepository.findByCustomMood(keyword);
        if(customMood != null) {
            List<Exhibition> exhibitions = moodRelationTestRepository.findByCustomMoodContaining(customMood);
            for(Exhibition exhibition: exhibitions) {
                long exhibition_id = exhibition.getExhibitionId();
                if (search_score_record.containsKey(exhibition_id))
                    search_score_record.put(exhibition_id,search_score_record.get(exhibition_id) + 2);
                else{
                    search_score_record.put(exhibition_id,2);}
            }
        }
        return "customMood ok";
    }

    private void searchByExhibitionName(String keyword) {
        Optional<List<Exhibition>> exhibitions = Optional.ofNullable(exhibitionRepository.findByTitleContaining(keyword));

        if (exhibitions.isPresent()) {
            for (Exhibition exhibition: exhibitions.get()) {
                long exhibition_id = exhibition.getExhibitionId();
                if (search_score_record.containsKey(exhibition_id))
                    search_score_record.put(exhibition_id,search_score_record.get(exhibition_id) + 5);
                else{
                    search_score_record.put(exhibition_id,5);}
            }
        }

    }

    private void searchByExhibitionContent(String keyword) {
        List<Exhibition> exhibitions = exhibitionRepository.findByContentContaining(keyword);

        if (exhibitions != null) {
            for (Exhibition exhibition: exhibitions) {
                long exhibition_id = exhibition.getExhibitionId();
                if (search_score_record.containsKey(exhibition_id))
                    search_score_record.put(exhibition_id,search_score_record.get(exhibition_id) + 3);
                else{
                    search_score_record.put(exhibition_id,3);}
            }
        }

    }

    private void searchByUsername(String keyword) {
        List<User> users = userTestRepository.findByNickname(keyword);
        if(users != null) {
            for(User user: users) {
                List<Exhibition> exhibitions = exhibitionRepository.findByUser(user);
                if (exhibitions != null) {
                    for (Exhibition exhibition: exhibitions) {
                        long exhibition_id = exhibition.getExhibitionId();
                        if (search_score_record.containsKey(exhibition_id))
                            search_score_record.put(exhibition_id,search_score_record.get(exhibition_id) + 4);
                        else{
                            search_score_record.put(exhibition_id,4);}
                    }
                }

            }
        }
    }

}
