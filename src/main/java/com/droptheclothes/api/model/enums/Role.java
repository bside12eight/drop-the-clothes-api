package com.droptheclothes.api.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
  USER("ROLE_USER", "일반 사용자"),
  ADMIN("ROLE_ADMIN", "관리자"),
  NONE("ROLE_GUEST", "손님");

  private final String code;
  private final String name;

  public static Role of(String code) {
    return Arrays.stream(Role.values())
        .filter(r -> r.getCode().equals(code))
        .findAny()
        .orElse(NONE);
  }
}