package com.koss.photocarpet.domain.customMood;
import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.moodGroup.MoodGroup;
import com.koss.photocarpet.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name="custom_mood")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomMood extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long customMoodId;

    @Column
    private String customMood;



    @OneToMany(mappedBy = "customMood", orphanRemoval = true)
    List<MoodGroup> moodGroups= new ArrayList<MoodGroup>();}

