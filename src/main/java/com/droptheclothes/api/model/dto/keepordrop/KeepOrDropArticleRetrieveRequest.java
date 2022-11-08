package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.model.enums.OrderType;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class KeepOrDropArticleRetrieveRequest {

    private OrderType orderType;

    private String category;

    public boolean checkArgumentValidation() {
        if (Objects.isNull(orderType)) {
            throw new IllegalArgumentException("정렬 방법을 정해주세요.");
        }

        if (StringUtils.isBlank(category)) {
            throw new IllegalArgumentException("카테고리를 입력해주세요.");
        }
        return true;
    }
}
