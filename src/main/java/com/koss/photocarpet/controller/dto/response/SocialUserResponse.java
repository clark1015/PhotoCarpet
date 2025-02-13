package com.koss.photocarpet.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserResponse {
    private Long userId;
    private String nickname;
    private String email;

    private String profileUrl;

    private String jwtToken;

    private String accessToken;

    private String profileMessage;

    private boolean validate_check;

    public SocialUserResponse(Long id, String nickname, String email){
        this.userId = id;
        this.nickname = nickname;
        this.email = email;
    }

}
