package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.geocoding.Coordinate;
import com.droptheclothes.api.model.dto.geocoding.CoordinateResponse;
import com.droptheclothes.api.utility.MessageConstants;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    public static final String ADDRESS_FLAG = "F02";

    public static final String VERSION = "F02";

    public static final String FORMAT = "json";

    @Value("${sk-open-api.app-key}")
    private String appKey;

    @Value("${sk-open-api.geocoding.host}")
    private String host;

    @Value("${sk-open-api.geocoding.endpoint}")
    private String endpoint;

    public Coordinate findCoordinateByAddress(String address) {
        CoordinateResponse coordinateResponse;

        try {
            coordinateResponse = WebClient.builder()
                    .baseUrl(host)
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(endpoint)
                            .queryParam("addressFlag", ADDRESS_FLAG)
                            .queryParam("version", VERSION)
                            .queryParam("format", FORMAT)
                            .queryParam("appKey", appKey)
                            .queryParam("fullAddr", address)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<CoordinateResponse>() {
                    })
                    .block();
        } catch (Exception e) {
            throw new IllegalArgumentException(MessageConstants.INVALID_ADDRESS_MESSAGE);
        }

        if (Objects.isNull(coordinateResponse)
                || Objects.isNull(coordinateResponse.getCoordinateInfo())
                || Objects.isNull(coordinateResponse.getCoordinateInfo().getCoordinate())
                || coordinateResponse.getCoordinateInfo().getCoordinate().isEmpty()) {
            throw new IllegalArgumentException(MessageConstants.INVALID_ADDRESS_MESSAGE);
        }

        return coordinateResponse.getCoordinateInfo().getCoordinate().get(0);
    }
}
