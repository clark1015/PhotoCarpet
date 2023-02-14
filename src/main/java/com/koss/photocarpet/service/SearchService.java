package com.koss.photocarpet.service;

import com.koss.photocarpet.domain.customMood.CustomMoodTestRepository;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import com.koss.photocarpet.domain.exhibition.ExhibitionTestRepository;
import com.koss.photocarpet.domain.moodGroup.MoodGroupTestRepository;
import com.koss.photocarpet.domain.user.User;
import com.koss.photocarpet.domain.user.UserTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class SearchService {
    @Autowired
    private CustomMoodTestRepository customMoodTestRepository;
    @Autowired
    private ExhibitionTestRepository exhibitionTestRepository;
    @Autowired
    private MoodGroupTestRepository moodGroupTestRepository;
    @Autowired
    private UserTestRepository userTestRepository;

    Map<Exhibition, Integer> search_score_record = new HashMap<Exhibition, Integer>();
    public LinkedHashSet<Exhibition> search(String keyword) {

        searchByCustomMood(keyword);
        searchByExhibitionName(keyword);
        searchByUsername(keyword);

        LinkedHashSet<Exhibition> searchResult = new LinkedHashSet<>();

        List<Exhibition> keySet = new ArrayList<>(search_score_record.keySet());
        keySet.sort((o1,o2) -> search_score_record.get(o2).compareTo(search_score_record.get(o1)));

        for (Exhibition exhibition: keySet) {
            searchResult.add(exhibition);
        }
        return searchResult;
    }

    private void searchByCustomMood(String keyword) {
        Optional<?> customMood = Optional.ofNullable(customMoodTestRepository.findByCustomMood(keyword));
        if(customMood.isPresent()) {
            List<Exhibition> exhibitions = moodGroupTestRepository.findByCustomMoodContaining(customMood);
            for(Exhibition exhibition: exhibitions) {
                if (search_score_record.containsKey(exhibition))
                    search_score_record.put(exhibition,search_score_record.get(exhibition) + 1);
                else{
                    search_score_record.put(exhibition,1);}
            }

        }
    }

    private void searchByExhibitionName(String keyword) {
        Optional<List<Exhibition>> exhibitions = Optional.ofNullable(exhibitionTestRepository.findByTitleContaining(keyword));

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
                Optional<List<Exhibition>> exhibitions = Optional.ofNullable(exhibitionTestRepository.findByUser(user));
                if (exhibitions.isPresent()) {
                    for (Exhibition exhibition: exhibitions.get()) {
                        if (search_score_record.containsKey(exhibition))
                            search_score_record.put(exhibition,search_score_record.get(exhibition) + 2);
                        else{
                            search_score_record.put(exhibition,2);}
                    }
                }

            }
        }
    }

}
