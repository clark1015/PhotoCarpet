package com.koss.photocarpet.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String accessToken;
    private String profileurl;
    private String profileMessage;
}
