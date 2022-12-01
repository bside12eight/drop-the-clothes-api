package com.droptheclothes.api.model.entity.pk;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ChargeId implements Serializable {

    private String member;

    private Long article;
}
