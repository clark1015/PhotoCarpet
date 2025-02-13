package com.koss.photocarpet.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInExhibition {
    private String nickName;
    private String profilUrl;

    public static UserInExhibition of(String nickName, String profilUrl) {
        return UserInExhibition.builder()
                .nickName(nickName)
                .profilUrl(profilUrl)
                .build();
    }
}
