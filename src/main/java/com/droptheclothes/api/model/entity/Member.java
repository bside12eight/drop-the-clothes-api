package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.base.BaseTimeEntity;
import com.droptheclothes.api.model.enums.Role;
import com.droptheclothes.api.utility.MessageConstants;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    private String profileImage;

    private boolean isRemoved;

    //email,nickName, provide, providerId)
    public static Member createMember(String providerId, String provider, String nickName, String email) {
        return Member.builder()
                .memberId(providerId)
                .provider(provider)
                .role(Role.USER)
                .email(email)
                .nickName(nickName)
                .build();
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void removeMember() {
        this.isRemoved = true;
    }

    public void changePassword(String currentPassword, String password) {
        if (!currentPassword.equals(this.password)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_PASSWORD_MESSAGE);
        }
        this.password = password;
    }

    public boolean isRemoved() {
        return isRemoved;
    }
}
