package com.droptheclothes.api.config;

import com.droptheclothes.api.jwt.JwtTokenProvider;
import com.droptheclothes.api.security.AuthenticatedUser;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        String memberId = tokenProvider.getPayload(authorization);
        SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(memberId));
        chain.doFilter(request,response);
    }
}
