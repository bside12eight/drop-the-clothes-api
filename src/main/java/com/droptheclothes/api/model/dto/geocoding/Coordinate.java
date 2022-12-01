package com.droptheclothes.api.model.dto.geocoding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {

    private String city_do;

    private String gu_gun;

    private String buildingName;

    private String buildingDong;

    private String newLat;

    private String newLon;

    private String newRoadName;

    private String newBuildingIndex;

    private String newBuildingName;

    private String zipcode;
}
