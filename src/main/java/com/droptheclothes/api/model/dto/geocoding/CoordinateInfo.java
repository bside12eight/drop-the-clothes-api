package com.droptheclothes.api.model.dto.geocoding;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateInfo {

    private String coordType;

    private String addressFlag;

    private String page;

    private String count;

    private String totalCount;

    private List<Coordinate> coordinate;
}
