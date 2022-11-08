package com.droptheclothes.api.controller;

import com.droptheclothes.api.model.base.ApiResponse;
import com.droptheclothes.api.model.base.ApiResponseHeader;
import com.droptheclothes.api.model.base.CollectionObject;
import com.droptheclothes.api.model.base.SingleObject;
import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.enums.ResultCode;
import com.droptheclothes.api.model.enums.VoteType;
import com.droptheclothes.api.service.KeepOrDropService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class KeepOrDropController {

    private final KeepOrDropService keepOrDropService;

    @PostMapping(value = "/api/keep-or-drop/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse registerKeepOrDropArticle(@RequestPart KeepOrDropArticleRegisterRequest request,
                                                 @RequestPart List<MultipartFile> files) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/keep-or-drop")
    public ApiResponse getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
        List<KeepOrDropArticleResponse> dummyResponse = new ArrayList<>();
        dummyResponse.add(KeepOrDropArticleResponse.builder().articleId(1L).category("가방").title("title1").description("desc1").build());
        dummyResponse.add(KeepOrDropArticleResponse.builder().articleId(2L).category("옷").title("title2").description("desc2").build());

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(dummyResponse));
    }

    @GetMapping("/api/keep-or-drop/{articleId}")
    public ApiResponse getKeepOrDropArticle(@PathVariable Long articleId) {
        KeepOrDropArticleResponse dummyResponse = KeepOrDropArticleResponse.builder()
                .articleId(1L)
                .category("가방")
                .title("title1")
                .description("desc1")
                .keepCount(16)
                .dropCount(4)
                .nickname("nickname1")
                .commentCount(5)
                .images(Arrays.asList("https://helpx.adobe.com/content/dam/help/en/photoshop/using/quick-actions/remove-background-before-qa1.png",
                                      "https://i.pinimg.com/564x/32/f2/43/32f24381b05fcf53d8088c98963fe326.jpg"))
                .createdAt(LocalDateTime.now())
                .build();

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new SingleObject<>(dummyResponse));
    }

    @PostMapping("/api/keep-or-drop/{articleId}/comments")
    public ApiResponse registerArticleComment(@PathVariable Long articleId, String comment) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/keep-or-drop/{articleId}/comments")
    public ApiResponse getArticleComments(@PathVariable Long articleId) {
        ArticleCommentResponse firstChild = ArticleCommentResponse.builder().nickname("child1").comment("child1").createdAt(LocalDateTime.now()).build();
        ArticleCommentResponse secondChild = ArticleCommentResponse.builder().nickname("child2").comment("child2").createdAt(LocalDateTime.now()).build();

        ArticleCommentResponse firstParent = ArticleCommentResponse.builder().nickname("parent1").comment("parent1").children(Arrays.asList(firstChild, secondChild)).createdAt(LocalDateTime.now()).build();
        ArticleCommentResponse secondParent = ArticleCommentResponse.builder().nickname("parent2").comment("parent2").createdAt(LocalDateTime.now()).build();

        List<ArticleCommentResponse> dummyResponse = new ArrayList<>();
        dummyResponse.add(firstParent);
        dummyResponse.add(secondParent);

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(dummyResponse));
    }

    @PostMapping("/api/keep-or-drop/{articleId}/vote")
    public ApiResponse voteKeepOrDrop(@PathVariable Long articleId, @RequestParam VoteType voteType) {
        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), null);
    }

    @GetMapping("/api/keep-or-drop/categories")
    public ApiResponse getItemCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("가방");
        categories.add("옷");

        return new ApiResponse(ApiResponseHeader.create(ResultCode.SUCCESS), new CollectionObject<>(categories));
    }
}
