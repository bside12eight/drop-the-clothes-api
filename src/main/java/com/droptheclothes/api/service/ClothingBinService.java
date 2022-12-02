package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinResponse;
import com.droptheclothes.api.model.entity.ClothingBin;
import com.droptheclothes.api.repository.ClothingBinRepository;
import com.droptheclothes.api.utility.BusinessConstants;
import com.droptheclothes.api.utility.MessageConstants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothingBinService {

    private final ClothingBinRepository clothingBinRepository;

    public List<ClothingBinResponse> getClothingBinsWithinRadius(Double latitude, Double longitude) {
        return clothingBinRepository.getClothingBinsWithinRadius(latitude, longitude, BusinessConstants.CLOTHING_BIN_SEARCH_RADIUS);
    }

    public ClothingBinResponse getClothingBin(Long clothingBinId) {
        ClothingBin clothingBin = clothingBinRepository.findByClothingBinId(clothingBinId)
                                                       .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));
        return ClothingBinResponse.entityToDto(clothingBin);
    }
}
