package com.droptheclothes.api.security;

import com.droptheclothes.api.exception.AuthenticationException;
import com.droptheclothes.api.utility.MessageConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginCheckAspect {

    @Before("@annotation(com.droptheclothes.api.security.LoginCheck)")
    public void checkAuthentication(JoinPoint joinPoint) {
        if (!SecurityUtility.isAuthenticated()) {
            throw new AuthenticationException(MessageConstants.NO_AUTHORITY_MESSAGE);
        }
    }
}
