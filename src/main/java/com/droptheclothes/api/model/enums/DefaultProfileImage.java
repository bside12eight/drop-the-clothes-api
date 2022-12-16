package com.droptheclothes.api.model.enums;

import lombok.Getter;

public enum DefaultProfileImage {
    default1("옷이라퍼", "https://kr.object.ncloudstorage.com/drop-the-clothes/profile/default/default1.png"),
    default2("우유부단", "https://kr.object.ncloudstorage.com/drop-the-clothes/profile/default/default2.png"),
    default3("의세권주민", "https://kr.object.ncloudstorage.com/drop-the-clothes/profile/default/default3.png"),
    default4("풀소유", "https://kr.object.ncloudstorage.com/drop-the-clothes/profile/default/default4.png"),
    default5("프로나눔러", "https://kr.object.ncloudstorage.com/drop-the-clothes/profile/default/default5.png"),
    default6("프로이사러", "https://kr.object.ncloudstorage.com/drop-the-clothes/profile/default/default6.png");

    private final String name;

    @Getter
    private final String imageUrl;

    DefaultProfileImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
