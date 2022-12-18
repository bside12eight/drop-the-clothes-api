package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinResponse;
import com.droptheclothes.api.model.dto.geocoding.Coordinate;
import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
        name = "ClothingBinsResponse",
        classes = @ConstructorResult(
                targetClass = ClothingBinResponse.class,
                columns = {
                        @ColumnResult(name = "clothingBinId", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "address", type = String.class),
                        @ColumnResult(name = "cityDo", type = String.class),
                        @ColumnResult(name = "guGun", type = String.class),
                        @ColumnResult(name = "roadName", type = String.class),
                        @ColumnResult(name = "buildingIndex", type = String.class),
                        @ColumnResult(name = "detailedAddress", type = String.class),
                        @ColumnResult(name = "latitude", type = Double.class),
                        @ColumnResult(name = "longitude", type = Double.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "distanceInMeters", type = Double.class),
                        @ColumnResult(name = "createdAt", type = LocalDateTime.class),
                        @ColumnResult(name = "updatedAt", type = LocalDateTime.class)
                }
        )
)
public class ClothingBin {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long clothingBinId;

    private String name;

    private String address;

    private String cityDo;

    private String guGun;

    private String roadName;

    private String buildingIndex;

    private String detailedAddress;

    private Double latitude;

    private Double longitude;

    private String image;

    private Boolean active;

    private Integer chargedCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ClothingBin updateClothingBin(Report report, String firstImagePath, Coordinate coordinate) {
        this.address = report.getAddress();
        this.cityDo = coordinate.getCity_do();
        this.guGun = coordinate.getGu_gun();
        this.roadName = coordinate.getNewRoadName();
        this.buildingIndex = coordinate.getNewBuildingIndex();
        this.detailedAddress = report.getDetailedAddress();
        this.latitude = report.getLatitude();
        this.longitude = report.getLongitude();
        this.image = firstImagePath;
        this.active = true;
        this.chargedCount = 0;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public ClothingBin inactiveClothingBin() {
        this.active = false;
        return this;
    }
}
