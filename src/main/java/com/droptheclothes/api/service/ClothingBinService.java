package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.ClothingBinResponse;
import com.droptheclothes.api.model.entity.ClothingBin;
import com.droptheclothes.api.repository.ClothingBinRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothingBinService {

    private final ClothingBinRepository clothingBinRepository;

    public List<ClothingBinResponse> getClothingBins(Double latitude, Double longitude) {
        List<ClothingBin> clothingBins = clothingBinRepository.findAll();
        return clothingBins.stream().map(ClothingBinResponse::entityToDto).collect(Collectors.toList());
    }
}
