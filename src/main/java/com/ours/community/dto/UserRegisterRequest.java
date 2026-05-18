package com.ours.community.dto;

import com.ours.community.domain.user.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterRequest {

    private String email;
    private Gender gender;
    private String provider;
    private String providerId;

}
