package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinResponse;
import java.util.List;

public interface ClothingBinRepositoryCustom {

    List<ClothingBinResponse> getClothingBinsWithinRadius(Double latitude, Double longitude, Integer radius);
}
