package com.droptheclothes.api.model.dto;

import lombok.Getter;

@Getter
public class CoordinateResponse {

    private int positionX;

    private int positionY;

    public CoordinateResponse(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
