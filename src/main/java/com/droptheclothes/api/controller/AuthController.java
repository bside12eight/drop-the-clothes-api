package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.AuthInfoResponse;
import com.droptheclothes.api.model.enums.ResultCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @PostMapping("/api/oauth/login")
  public ApiResponse getAuthInfo(@RequestBody AuthInfoResponse response) {
    return new ApiResponse(
        ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(new AuthInfoResponse(response.getAuthId(), response.getAuthType(),
        response.getDeviceID()))
    );
  }

}
