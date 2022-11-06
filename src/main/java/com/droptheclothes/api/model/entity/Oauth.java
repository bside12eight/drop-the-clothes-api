package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.base.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
public class Oauth extends BaseTimeEntity {

  @Id
  private String authId; // accesssToken
  private String authType; // kakao, apple
  private String deviceID; // 디바이스 정보는 회원가입할 때 받기

  @Builder
  public Oauth(String authId,String authType, String deviceID){
    this.authId = authId;
    this.authType = authType;
    this.deviceID = deviceID;
  }

}