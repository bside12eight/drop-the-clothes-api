package com.droptheclothes.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportClothingBinRequest {

    private Long memberId;

    private String address;

    private String detailedAddress;

    private double latitude;

    private double longitude;

    private String comment;
}
