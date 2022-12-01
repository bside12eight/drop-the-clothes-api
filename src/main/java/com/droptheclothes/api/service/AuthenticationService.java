package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.auth.JoinRequest;
import com.droptheclothes.api.model.dto.auth.LoginRequest;
import com.droptheclothes.api.model.dto.auth.LoginResponse;
import com.droptheclothes.api.model.enums.LoginProviderType;

public interface AuthenticationService {

    Object login(LoginRequest request);

    LoginResponse signUp(LoginProviderType provider,
            JoinRequest request);
}
