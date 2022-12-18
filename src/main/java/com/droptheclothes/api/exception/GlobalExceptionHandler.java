package com.droptheclothes.api.exception;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.enums.ResultCode;
import java.net.BindException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class, BindException.class})
    public ApiResponse handleIllegalArgumentException(HttpServletRequest request, Exception exception) {
        writeWarningLog(request, exception);
        return createResponse(ResultCode.BAD_REQUEST, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({AuthenticationException.class})
    public ApiResponse handleAuthenticationException(HttpServletRequest request, Exception exception) {
        writeWarningLog(request, exception);
        return createResponse(ResultCode.UNAUTHORIZED, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({AppleRequestException.class})
    public ApiResponse handleAppleRequestException(HttpServletRequest request, Exception exception) {
        writeWarningLog(request, exception);
        return createResponse(ResultCode.BAD_REQUEST, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({ObjectStorageException.class})
    public ApiResponse handleObjectStorageException(HttpServletRequest request, ObjectStorageException exception) {
        writeErrorLog(request, exception);
        return createResponse(ResultCode.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ApiResponse createResponse(ResultCode resultCode) {
        return new ApiResponse(ApiResponseHeader.create(resultCode), null);
    }

    private ApiResponse createResponse(ResultCode resultCode, String message) {
        return new ApiResponse(ApiResponseHeader.create(resultCode, message), null);
    }

    private void writeErrorLog(HttpServletRequest request, Exception e) {
        log.error("method: {}, request URI: {}", request.getMethod(), request.getRequestURI(), e);
    }

    private void writeWarningLog(HttpServletRequest request, Exception e) {
        log.warn("method: {}, request URI: {}", request.getMethod(), request.getRequestURI(), e);
    }
}
