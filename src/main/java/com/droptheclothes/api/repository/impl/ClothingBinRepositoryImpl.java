package com.droptheclothes.api.repository.impl;

import com.droptheclothes.api.model.dto.clothingbin.ClothingBinResponse;
import com.droptheclothes.api.repository.ClothingBinRepositoryCustom;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ClothingBinRepositoryImpl implements ClothingBinRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ClothingBinResponse> getClothingBinsWithin1km(Double latitude, Double longitude, Integer distance) {
        return entityManager.createNativeQuery("select clothingBinId,\n" +
                                        "       name,\n" +
                                        "       address,\n" +
                                        "       detailedAddress,\n" +
                                        "       latitude,\n" +
                                        "       longitude,\n" +
                                        "       ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(longitude, latitude)) as distanceInMeters,\n" +
                                        "       updatedAt\n" +
                                        "  from ClothingBin\n" +
                                        " where ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(longitude, latitude)) <= :distance" +
                                        " order by distanceInMeters asc", "ClothingBinResponse")
                     .setParameter("latitude", latitude)
                     .setParameter("longitude", longitude)
                     .setParameter("distance", distance)
                     .getResultList();
    }
}
