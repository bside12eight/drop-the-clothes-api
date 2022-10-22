package com.droptheclothes.api.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmRequest {
  private String targetToken;
  private String title;
  private String body;
}
