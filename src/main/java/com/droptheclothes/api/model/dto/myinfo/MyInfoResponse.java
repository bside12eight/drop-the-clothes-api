package com.droptheclothes.api.model.dto.myinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyInfoResponse {

    private String email;

    private String nickname;

    private String profileImage;
}
