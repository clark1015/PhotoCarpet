package com.koss.photocarpet.controller.dto.response;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientTestResponse {
    private Long index;
    private String imageURl;

    public ClientTestResponse(String s) {
        this.imageURl = s;
        this.index += 1;

    }
}
