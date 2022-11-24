package com.droptheclothes.api.model.dto.myinfo;

import com.droptheclothes.api.model.entity.BlockedMember;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockedUserResponse {

    private String memberId;

    private String nickName;

    private LocalDateTime createdAt;

    public static List<BlockedUserResponse> of(List<BlockedMember> blockedMembers) {
        return blockedMembers.stream().map(blockedMember -> BlockedUserResponse.builder()
                .memberId(blockedMember.getBlockedMember().getMemberId())
                .nickName(blockedMember.getBlockedMember().getNickName())
                .createdAt(blockedMember.getCreatedAt())
                .build())
                .collect(Collectors.toList());
    }
}
