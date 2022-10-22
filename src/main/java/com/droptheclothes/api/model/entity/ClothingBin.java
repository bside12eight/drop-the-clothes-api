package com.droptheclothes.api.model.entity;

import com.droptheclothes.api.model.dto.ClothingBinResponse;
import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import lombok.Getter;

@Getter
@Entity
@SqlResultSetMapping(
    name = "ClothingBinResponse",
    classes = @ConstructorResult(
            targetClass = ClothingBinResponse.class,
            columns = {
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name ="address", type = String.class),
                @ColumnResult(name = "detailedAddress", type = String.class),
                @ColumnResult(name = "latitude", type = Double.class),
                @ColumnResult(name = "longitude", type = Double.class),
                @ColumnResult(name = "distanceInMeters", type = Double.class),
                @ColumnResult(name = "updatedAt", type = LocalDateTime.class)
            }
    )
)
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
