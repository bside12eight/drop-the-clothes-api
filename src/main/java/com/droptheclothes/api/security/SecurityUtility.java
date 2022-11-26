package com.droptheclothes.api.security;

import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtility {

    public static boolean isAuthenticated() {
        return !Objects.isNull(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static String getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Objects.isNull(authentication) ? null : authentication.getName();
    }
}
