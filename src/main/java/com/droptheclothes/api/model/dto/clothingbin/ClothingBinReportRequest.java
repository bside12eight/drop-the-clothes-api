package com.droptheclothes.api.model.dto.clothingbin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class ClothingBinReportRequest {

    @Schema(required = true)
    private String address;

    private String detailedAddress;

    private String comment;

    public boolean checkArgumentValidation() {
        if (StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("주소를 입력해주세요.");
        }
        return true;
    }
}
