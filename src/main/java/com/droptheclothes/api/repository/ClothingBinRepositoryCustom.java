package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinResponse;
import java.util.List;

public interface ClothingBinRepositoryCustom {

    List<ClothingBinResponse> getClothingBinsWithin1km(Double latitude, Double longitude, Integer distance);
}
