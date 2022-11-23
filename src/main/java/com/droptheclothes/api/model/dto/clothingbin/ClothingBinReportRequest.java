package com.droptheclothes.api.model.dto.clothingbin;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClothingBinReportRequest {

    @Schema(required = true)
    private String address;

    @Schema(required = true)
    private String detailedAddress;

    @Schema(required = true)
    private Double latitude;

    @Schema(required = true)
    private Double longitude;

    private String comment;

    public boolean checkArgumentValidation() {
        if (StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("주소를 입력해주세요.");
        }

        if (Objects.isNull(latitude) || Objects.isNull(longitude)) {
            throw new IllegalArgumentException("위도 및 경도를 입력해주세요.");
        }
        return true;
    }
}
