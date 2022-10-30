package com.droptheclothes.api.repository;

import com.droptheclothes.api.model.entity.ClothingBin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingBinRepository extends JpaRepository<ClothingBin, Long>, ClothingBinRepositoryCustom {

    Optional<ClothingBin> findByClothingBinId(Long clothingBinId);

    Optional<ClothingBin> findByAddress(String address);
}
