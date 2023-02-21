package com.koss.photocarpet.controller.dto.response;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialUserResponse {
    private Long userId;
    private String nickname;
    private String email;

    private String token;

    public SocialUserResponse(Long id, String nickname, String email){
        this.userId = id;
        this.nickname = nickname;
        this.email = email;
    }

}
