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
    public List<ClothingBinResponse> getClothingBinsWithinRadius(Double latitude, Double longitude, Integer radius) {
        return entityManager.createNativeQuery("select clothingBinId,\n" +
                                        "       name,\n" +
                                        "       address,\n" +
                                        "       detailedAddress,\n" +
                                        "       latitude,\n" +
                                        "       longitude,\n" +
                                        "       image,\n" +
                                        "       ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(longitude, latitude)) as distanceInMeters,\n" +
                                        "       createdAt,\n" +
                                        "       updatedAt\n" +
                                        "  from ClothingBin\n" +
                                        " where ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(longitude, latitude)) <= :radius" +
                                        "   and active is true" +
                                        " order by distanceInMeters asc", "ClothingBinsResponse")
                     .setParameter("latitude", latitude)
                     .setParameter("longitude", longitude)
                     .setParameter("radius", radius)
                     .getResultList();
    }
}
