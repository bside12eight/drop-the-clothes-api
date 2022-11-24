package com.droptheclothes.api.model.entity.pk;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BlockedMemberId implements Serializable {

    private String member;

    private String blockedMember;
}
