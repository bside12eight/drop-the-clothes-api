package com.droptheclothes.api.model.dto.keepordrop;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeepOrDropArticleResponse {

    private String title;

    private String description;

    private LocalDateTime createdAt;
}
