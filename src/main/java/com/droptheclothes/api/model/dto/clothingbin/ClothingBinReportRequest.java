package com.droptheclothes.api.model.dto.clothingbin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothingBinReportRequest {

    private String memberId;

    private String address;

    private String detailedAddress;

    private double latitude;

    private double longitude;

    private String comment;
}
