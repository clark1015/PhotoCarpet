package com.koss.photocarpet.domain.moodRelation;

import com.koss.photocarpet.domain.BaseTimeEntity;
import com.koss.photocarpet.domain.customMood.CustomMood;
import com.koss.photocarpet.domain.exhibition.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name="moodgroup")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoodRelation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long moodGroupId;

    @ManyToOne
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    @ManyToOne
    @JoinColumn(name = "custom_mood")
    private CustomMood customMood;

}
