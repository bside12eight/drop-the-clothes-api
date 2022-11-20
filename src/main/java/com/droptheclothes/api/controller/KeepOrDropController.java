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
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.model.enums.VoteType;
import com.droptheclothes.api.service.KeepOrDropService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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

    private final int MAX_IMAGE_COUNT = 3;

    private final KeepOrDropService keepOrDropService;

    @PostMapping(value = "/api/keep-or-drop/article")
    public ApiResponse registerKeepOrDropArticle(@RequestPart KeepOrDropArticleRegisterRequest request,
                                                 @RequestPart(required = false) List<MultipartFile> images) {
        request.checkArgumentValidation();
        checkImageCountValidation(images);

        keepOrDropService.registerKeepOrDropArticle(request, images);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/keep-or-drop")
    public ApiResponse getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
        List<KeepOrDropArticleResponse> response = keepOrDropService.getKeepOrDropArticles(request);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @GetMapping("/api/keep-or-drop/{articleId}")
    public ApiResponse getKeepOrDropArticle(@PathVariable Long articleId) {
        KeepOrDropArticleResponse response = keepOrDropService.getKeepOrDropArticle(articleId);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(response));
    }

    @PostMapping("/api/keep-or-drop/{articleId}/comments")
    public ApiResponse registerArticleComment(@PathVariable Long articleId,
                                              @RequestBody ArticleCommentRegisterRequest request) {
        request.checkArgumentValidation();
        keepOrDropService.registerArticleComment(articleId, request);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/keep-or-drop/{articleId}/comments")
    public ApiResponse getArticleComments(@PathVariable Long articleId) {
        List<ArticleCommentResponse> response = keepOrDropService.getArticleComments(articleId);
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(response));
    }

    @PostMapping("/api/keep-or-drop/{articleId}/vote")
    public ApiResponse voteKeepOrDrop(@PathVariable Long articleId, @RequestParam(required = false) VoteType voteType) {
        if (Objects.isNull(voteType)) {
            throw new IllegalArgumentException("투표 타입을 입력해주세요.");
        }
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/keep-or-drop/categories")
    public ApiResponse getItemCategories() {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS),
                new CollectionObject<>(keepOrDropService.getItemCategories()));
    }

    private void checkImageCountValidation(List<MultipartFile> images) {
        if (!Objects.isNull(images) && images.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("이미지 파일은 최대 3개까지 업로드 가능합니다.");
        }
    }
}
