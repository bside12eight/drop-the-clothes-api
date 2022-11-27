package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.auth.LoginRequest;

public interface AuthenticationService {

    void login(LoginRequest request);

    void signUp();
}
