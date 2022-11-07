package com.droptheclothes.api.model.dto;


import com.droptheclothes.api.model.entity.ClothingBin;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClothingBinResponse {

    private Long clothingBinId;

    private String name;

    private String address;

    private String detailedAddress;

    private Double latitude;

    private Double longitude;

    @JsonInclude(Include.NON_NULL)
    private Double distanceInMeters;

    private LocalDateTime updatedAt;

    public static ClothingBinResponse entityToDto(ClothingBin entity) {
        return ClothingBinResponse.builder()
                                  .clothingBinId(entity.getClothingBinId())
                                  .name(entity.getName())
                                  .address(entity.getAddress())
                                  .detailedAddress(entity.getDetailedAddress())
                                  .latitude(entity.getLatitude())
                                  .longitude(entity.getLongitude())
                                  .updatedAt(entity.getUpdatedAt())
                                  .build();
    }

    @Builder
    public ClothingBinResponse(Long clothingBinId, String name, String address, String detailedAddress, Double latitude,
                               Double longitude, Double distanceInMeters, LocalDateTime updatedAt) {
        this.clothingBinId = clothingBinId;
        this.name = name;
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceInMeters = distanceInMeters;
        this.updatedAt = updatedAt;
    }
}
