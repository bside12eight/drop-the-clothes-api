package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.OauthInfoRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OauthController {

  private final OauthService oauthService;

  @PostMapping("/api/oauth/login")
  public ApiResponse saveAccessToken(@RequestBody OauthInfoRequest request) {

    oauthService.saveAccessToken(request);

    return new ApiResponse(
        ApiResponseHeader.create(ResultCode.SUCCESS)
        , new SingleObject<>(new OauthInfoRequest(request.getAuthId(), request.getAuthType(),
        request.getDeviceID()))
    );
  }

}
