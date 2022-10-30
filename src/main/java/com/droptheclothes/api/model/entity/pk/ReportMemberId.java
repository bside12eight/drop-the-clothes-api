package com.droptheclothes.api.model.entity.pk;

import java.io.Serializable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReportMemberId implements Serializable {

    private Long reportId;

    private Long memberId;
}
