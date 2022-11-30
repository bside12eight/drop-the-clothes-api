package com.droptheclothes.api.model.dto.clothingbin;

import com.droptheclothes.api.model.entity.ClothingBin;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ClothingBinResponse {

    private Long clothingBinId;

    private String name;

    private String address;

    private String detailedAddress;

    private Double latitude;

    private Double longitude;

    private String image;

    @JsonInclude(Include.NON_NULL)
    private Double distanceInMeters;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ClothingBinResponse entityToDto(ClothingBin entity) {
        return ClothingBinResponse.builder()
                .clothingBinId(entity.getClothingBinId())
                .name(entity.getName())
                .address(entity.getAddress())
                .detailedAddress(entity.getDetailedAddress())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .image(entity.getImage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
