package com.droptheclothes.api.model.dto.apple;

import lombok.Getter;

@Getter
public class IdentityTokenHeader {

    private String kid;

    private String alg;
}
