package com.droptheclothes.api.repository.impl;

import com.droptheclothes.api.model.dto.ClothingBinResponse;
import com.droptheclothes.api.repository.ClothingBinRepositoryCustom;
import java.util.List;

public class ClothingBinRepositoryImpl implements ClothingBinRepositoryCustom {

    @Override
    public List<ClothingBinResponse> getClothingBinsWithin3km(Double latitude, Double longitude) {
        return null;
    }
}
