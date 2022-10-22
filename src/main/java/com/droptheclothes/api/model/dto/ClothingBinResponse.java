package com.droptheclothes.api.model.dto;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ClothingBinResponse {

    private String name;

    private String address;

    private String detailedAddress;

    private Double latitude;

    private Double longitude;

    private Double distanceInMeters;

    private LocalDateTime updatedAt;

    public ClothingBinResponse(String name, String address, String detailedAddress,
                               Double latitude, Double longitude, Double distanceInMeters,
                               LocalDateTime updatedAt) {
        this.name = name;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceInMeters = distanceInMeters;
        this.updatedAt = updatedAt;
    }
}
