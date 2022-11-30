package com.droptheclothes.api.model.dto.myinfo;

import com.droptheclothes.api.model.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyInfoResponse {

    private String email;

    private String nickname;

    private String profileImage;

    public static MyInfoResponse of(Member member) {
        return MyInfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickName())
                .profileImage(member.getProfileImage())
                .build();
    }
}
