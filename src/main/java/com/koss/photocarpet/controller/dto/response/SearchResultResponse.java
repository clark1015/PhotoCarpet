package com.koss.photocarpet.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponse {
    private LinkedHashSet<?> result;
    private String error;
}
