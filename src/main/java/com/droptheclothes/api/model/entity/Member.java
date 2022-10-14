package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.base.BaseTimeEntity;
import java.time.LocalDateTime;
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
public class Member extends BaseTimeEntity {

  @Id
  private String memberId;
  private String joinType;
  private String nickName;
  private String email;
  private String password;
  private LocalDateTime loggedInAt;
  private LocalDateTime deletedAt;

  @Builder
  public Member(String memberId, String joinType, String nickName, String email, String password, LocalDateTime loggedInAt, LocalDateTime deletedAt){
    this.memberId = memberId;
    this.joinType = joinType;
    this.nickName = nickName;
    this.email = email;
    this.password = password;
    this.loggedInAt = loggedInAt;
    this.deletedAt = deletedAt;
  }

}
