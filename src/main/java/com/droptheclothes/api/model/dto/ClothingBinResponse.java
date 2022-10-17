package com.droptheclothes.api.model.dto;


import com.droptheclothes.api.model.entity.ClothingBin;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClothingBinResponse {

    private String name;

    private String address;

    private String detailedAddress;

    private Double latitued;

    private Double longitude;

    private Double distance;

    private LocalDateTime updatedAt;

    public static ClothingBinResponse entityToDto(ClothingBin clothingBin) {
        return ClothingBinResponse.builder()
                                  .name(clothingBin.getName())
                                  .address(clothingBin.getAddress())
                                  .detailedAddress(clothingBin.getDetailedAddress())
                                  .latitued(clothingBin.getLatitude())
                                  .longitude(clothingBin.getLongitude())
                                  .updatedAt(clothingBin.getUpdatedAt())
                                  .build();
    }
}
