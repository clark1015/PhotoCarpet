package com.koss.photocarpet.service;

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

    Map<Exhibition, Integer> search_score_record;
    public LinkedHashSet<Exhibition> search(String keyword) {
        search_score_record = new HashMap<Exhibition, Integer>();
        Pattern pattern = Pattern.compile("\\s+");

        String[] splitKeywordBySpace = pattern.split(keyword);
        List<String> splitKeywordBySpaceList = Arrays.asList(splitKeywordBySpace);

        for(String splitKeyword: splitKeywordBySpaceList) {
            searchByCustomMood(splitKeyword);
            searchByExhibitionName(splitKeyword);
            searchByUsername(splitKeyword);
            searchByExhibitionContent(splitKeyword);
        }

        LinkedHashSet<Exhibition> searchResult = new LinkedHashSet<>();

        List<Exhibition> keySet = new ArrayList<>(search_score_record.keySet());
        keySet.sort((o1,o2) -> search_score_record.get(o2).compareTo(search_score_record.get(o1)));

        System.out.println(search_score_record);

        for (Exhibition exhibition: keySet) {
            searchResult.add(exhibition);
        }
        return searchResult;
    }

    private String searchByCustomMood(String keyword) {
        CustomMood customMood = customMoodTestRepository.findByCustomMood(keyword);
        if(customMood != null) {
            List<Exhibition> exhibitions = moodRelationTestRepository.findByCustomMoodContaining(customMood);
            for(Exhibition exhibition: exhibitions) {
                if (search_score_record.containsKey(exhibition))
                    search_score_record.put(exhibition,search_score_record.get(exhibition) + 2);
                else{
                    search_score_record.put(exhibition,2);}
            }
        }
        return "customMood ok";
    }

    private void searchByExhibitionName(String keyword) {
        Optional<List<Exhibition>> exhibitions = Optional.ofNullable(exhibitionRepository.findByTitleContaining(keyword));

        if (exhibitions.isPresent()) {
            for (Exhibition exhibition: exhibitions.get()) {
                if (search_score_record.containsKey(exhibition))
                    search_score_record.put(exhibition,search_score_record.get(exhibition) + 5);
                else{
                    search_score_record.put(exhibition,5);}
            }
        }

    }

    private void searchByExhibitionContent(String keyword) {
        Optional<List<Exhibition>> exhibitions = Optional.ofNullable(exhibitionRepository.findByContentContaining(keyword));

        if (exhibitions.isPresent()) {
            for (Exhibition exhibition: exhibitions.get()) {
                if (search_score_record.containsKey(exhibition))
                    search_score_record.put(exhibition,search_score_record.get(exhibition) + 3);
                else{
                    search_score_record.put(exhibition,3);}
            }
        }

    }

    private void searchByUsername(String keyword) {
        Optional<List<User>> users = Optional.ofNullable(userTestRepository.findByNickname(keyword));
        if(users.isPresent()) {
            for(User user: users.get()) {
                Optional<List<Exhibition>> exhibitions = Optional.ofNullable(exhibitionRepository.findByUser(user));
                if (exhibitions.isPresent()) {
                    for (Exhibition exhibition: exhibitions.get()) {
                        if (search_score_record.containsKey(exhibition))
                            search_score_record.put(exhibition,search_score_record.get(exhibition) + 4);
                        else{
                            search_score_record.put(exhibition,4);}
                    }
                }

            }
        }
    }

}
