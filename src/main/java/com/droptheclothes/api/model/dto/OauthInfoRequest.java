package com.droptheclothes.api.model.dto;

import com.droptheclothes.api.model.entity.Oauth;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OauthInfoRequest {

    //고유id, 어떤 간편로그인인지, 기기 정보
    private String authId; // kakao : accesssToken / apple : identity token
    private String authType; // kakao, apple
    private String deviceID; //

    @Builder
    public OauthInfoRequest(String authId, String authType, String deviceID) {
        this.authId = authId;
        this.authType = authType;
        this.deviceID = deviceID;
    }

    public Oauth toEntity() {
        return Oauth.builder()
                .authId(authId)
                .authType(authType)
                .deviceID(deviceID)
                .build();
    }

}
