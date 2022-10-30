package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.base.BaseTimeEntity;
import com.droptheclothes.api.model.enums.Role;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Member extends BaseTimeEntity {

  @Id
  private String memberId;

  private String provider; // joinType
  private String nickName;

  @Column(unique = true)
  private String email; // UQ

  private String password;
  private LocalDateTime loggedInAt;
  private LocalDateTime deletedAt;

  @Enumerated(EnumType.STRING)
  private Role role;


  @Builder
  public Member(
        String memberId
      , String provider
      , String nickName
      , String email
      , String password
      , LocalDateTime loggedInAt
      , LocalDateTime deletedAt
      , Role role
  ){
    this.memberId = memberId;
    this.provider = provider;
    this.nickName = nickName;
    this.email = email;
    this.password = password;
    this.loggedInAt = loggedInAt;
    this.deletedAt = deletedAt;
    this.role = role;
  }
  //email,nickName, provide, providerId)
  public static Member createMember(
      String providerId
      , String provide
      , String nickName
      , String email
  ){
    Member member = Member.builder()
        .memberId(providerId)
        .provider(provide)
        .role(Role.USER)
        .email(email)
        .nickName(nickName)
        .build();

    return member;
  }

}
