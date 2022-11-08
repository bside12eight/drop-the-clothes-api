package com.droptheclothes.api.exception;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.enums.ResultCode;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class})
    public ApiResponse handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException exception) {
        writeWarningLog(request);
        return createResponse(ResultCode.BAD_REQUEST, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({ObjectStorageException.class})
    public ApiResponse handleObjectStorageException(HttpServletRequest request, ObjectStorageException exception) {
        writeWarningLog(request);
        return createResponse(ResultCode.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ApiResponse createResponse(ResultCode resultCode) {
        return new ApiResponse(ApiResponseHeader.create(resultCode), null);
    }

    private ApiResponse createResponse(ResultCode resultCode, String message) {
        return new ApiResponse(ApiResponseHeader.create(resultCode, message), null);
    }

    private void writeWarningLog(HttpServletRequest request) {
        log.warn("method: {}, request URI: {}", request.getMethod(), request.getRequestURI());
    }
}
