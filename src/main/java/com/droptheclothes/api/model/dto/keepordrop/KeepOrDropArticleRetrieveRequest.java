package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.model.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeepOrDropArticleRetrieveRequest {

    private OrderType orderType;

    private String category;
}
