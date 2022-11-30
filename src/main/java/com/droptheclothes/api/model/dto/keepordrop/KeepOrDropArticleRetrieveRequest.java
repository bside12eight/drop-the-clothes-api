package com.droptheclothes.api.model.dto.keepordrop;

import com.droptheclothes.api.model.enums.OrderType;
import com.droptheclothes.api.utility.MessageConstants;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class KeepOrDropArticleRetrieveRequest {

    private OrderType orderType;

    private String category;

    private Integer page;

    public boolean checkArgumentValidation() {
        if (Objects.isNull(orderType) || StringUtils.isBlank(category) || Objects.isNull(page)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        return true;
    }

    public int getOffset(int pageSize) {
        return (this.page - 1) * pageSize;
    }
}
