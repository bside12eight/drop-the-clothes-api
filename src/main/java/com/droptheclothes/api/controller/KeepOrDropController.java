package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.enums.ChargeReasonType;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.model.enums.VoteType;
import com.droptheclothes.api.security.LoginCheck;
import com.droptheclothes.api.service.KeepOrDropService;
import com.droptheclothes.api.utility.BusinessConstants;
import com.droptheclothes.api.utility.MessageConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class KeepOrDropController {

    private final KeepOrDropService keepOrDropService;

    @LoginCheck
    @Operation(summary = "버릴까 말까 글 등록 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PostMapping(value = "/api/keep-or-drop/article")
    public ApiResponse registerKeepOrDropArticle(@RequestPart KeepOrDropArticleRegisterRequest request,
                                                 @RequestPart(required = false) List<MultipartFile> images) {
        checkImageCountValidation(images);

        keepOrDropService.registerKeepOrDropArticle(request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @Operation(summary = "버릴까 말까 글 목록 조회 API")
    @GetMapping("/api/keep-or-drop")
    public ApiResponse<List<KeepOrDropArticleResponse>> getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
        request.checkArgumentValidation();
        List<KeepOrDropArticleResponse> response = keepOrDropService.getKeepOrDropArticles(request);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @Operation(summary = "버릴까 말까 글 상세 조회 API")
    @GetMapping("/api/keep-or-drop/{articleId}")
    public ApiResponse<KeepOrDropArticleResponse> getKeepOrDropArticle(@PathVariable Long articleId) {
        KeepOrDropArticleResponse response = keepOrDropService.getKeepOrDropArticle(articleId);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(response));
    }

    @LoginCheck
    @Operation(summary = "버릴까 말까 댓글 등록 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PostMapping("/api/keep-or-drop/{articleId}/comments")
    public ApiResponse registerArticleComment(@PathVariable Long articleId,
                                              @RequestBody ArticleCommentRegisterRequest request) {
        keepOrDropService.registerArticleComment(articleId, request);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @LoginCheck
    @Operation(summary = "버릴까 말까 댓글 삭제 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @DeleteMapping("/api/keep-or-drop/{articleId}/comments/{commentId}")
    public ApiResponse deleteArticleComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        keepOrDropService.deleteArticleComment(articleId, commentId);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @Operation(summary = "버릴까 말까 댓글 목록 조회 API")
    @GetMapping("/api/keep-or-drop/{articleId}/comments")
    public ApiResponse<List<ArticleCommentResponse>> getArticleComments(@PathVariable Long articleId) {
        List<ArticleCommentResponse> response = keepOrDropService.getArticleComments(articleId);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @LoginCheck
    @Operation(summary = "버릴까 말까 투표 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PostMapping("/api/keep-or-drop/{articleId}/vote")
    public ApiResponse voteKeepOrDrop(@PathVariable Long articleId, @RequestParam(required = false) VoteType voteType) {
        if (Objects.isNull(voteType)) {
            throw new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE);
        }
        keepOrDropService.voteKeepOrDrop(articleId, voteType);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @Operation(summary = "카테고리 조회 API")
    @GetMapping("/api/keep-or-drop/categories")
    public ApiResponse<List<String>> getItemCategories() {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new CollectionObject<>(keepOrDropService.getItemCategories()));
    }

    @LoginCheck
    @Operation(summary = "버릴까 말까 글 신고 API")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer token", required = true, example = "Bearer {token value}")
    @PostMapping("/api/keep-or-drop/{articleId}")
    public ApiResponse blockKeepOrDropArticle(@PathVariable Long articleId, ChargeReasonType chargeReason) {
        keepOrDropService.blockKeepOrDropArticle(articleId, chargeReason);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    private void checkImageCountValidation(List<MultipartFile> images) {
        if (!Objects.isNull(images) && images.size() > BusinessConstants.MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(String.format(MessageConstants.MAX_IMAGE_COUNT_EXCEED_MESSAGE, BusinessConstants.MAX_IMAGE_COUNT));
        }
    }
}
