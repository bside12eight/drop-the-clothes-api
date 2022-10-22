package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.dto.ClothingBinResponse;
import java.util.List;

public interface ClothingBinRepositoryCustom {

    List<ClothingBinResponse> getClothingBinsWithin1km(Double latitude, Double longitude, Integer distance);
}
