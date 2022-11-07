package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.ClothingBinResponse;
import com.droptheclothes.api.model.entity.ClothingBin;
import com.droptheclothes.api.repository.ClothingBinRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothingBinService {

    private final ClothingBinRepository clothingBinRepository;

    public List<ClothingBinResponse> getClothingBinsWithin1km(Double latitude, Double longitude, Integer distance) {
        return clothingBinRepository.getClothingBinsWithin1km(latitude, longitude, distance);
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
