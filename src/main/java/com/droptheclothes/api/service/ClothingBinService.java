package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinResponse;
import com.droptheclothes.api.model.entity.ClothingBin;
import com.droptheclothes.api.repository.ClothingBinRepository;
import com.droptheclothes.api.utility.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothingBinService {

    private final ClothingBinRepository clothingBinRepository;

    public List<ClothingBinResponse> getClothingBinsWithinRadius(Double latitude, Double longitude) {
        return clothingBinRepository.getClothingBinsWithinRadius(latitude, longitude, Constants.CLOTHING_BIN_SEARCH_RADIUS);
    }

    public ClothingBinResponse getClothingBin(Long clothingBinId) {
        ClothingBin clothingBin = clothingBinRepository.findByClothingBinId(clothingBinId)
                                                       .orElseThrow(() -> new IllegalArgumentException("There is no matched clothing bin"));
        return ClothingBinResponse.entityToDto(clothingBin);
    }

    public boolean isRegisteredClothingBin(String address) {
        return clothingBinRepository.findByAddress(address).isPresent();
    }
}
