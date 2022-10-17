package com.droptheclothes.api.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class ClothingBin {

    @Id
    private Long clothingBinId;

    private String name;

    private String address;

    private String detailedAddress;

    private Double latitude;

    private Double longitude;

    private Boolean active;

    private Integer chargedCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
