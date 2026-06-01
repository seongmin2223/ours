package com.ours.community.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    private String title;
    private String content;
    private Integer userId;

}
