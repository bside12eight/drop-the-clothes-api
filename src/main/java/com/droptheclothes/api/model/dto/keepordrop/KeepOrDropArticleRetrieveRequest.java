package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.model.enums.OrderType;
import com.droptheclothes.api.utility.MessageConstants;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeepOrDropArticleRetrieveRequest {

    private OrderType orderType;

    private List<String> categories;

    private Integer page;

    public boolean checkArgumentValidation() {
        if (Objects.isNull(orderType) || Objects.isNull(page)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        return true;
    }

    public int getOffset(int pageSize) {
        return (this.page - 1) * pageSize;
    }
}
