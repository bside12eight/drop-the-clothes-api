package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.base.BaseTimeEntity;
import com.droptheclothes.api.model.enums.LoginProviderType;
import com.droptheclothes.api.model.enums.Role;
import com.droptheclothes.api.utility.MessageConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Enumerated(EnumType.STRING)
    private LoginProviderType provider;

    private String nickName;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDateTime loggedInAt;

    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    private boolean isRemoved;

    public static Member createMember(String providerId, LoginProviderType provider, String nickName, String email) {
        Member member = Member.builder()
                .memberId(String.format("%s_%s", providerId, LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)))
                .provider(provider)
                .role(Role.USER)
                .email(email)
                .nickName(nickName)
                .build();

        return member;
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void removeMember() {
        this.nickName = null;
        this.isRemoved = true;
        this.deletedAt = LocalDateTime.now();
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

    public boolean isSocialLoginMember() {
        return !provider.equals(LoginProviderType.email.toString());
    }
}
