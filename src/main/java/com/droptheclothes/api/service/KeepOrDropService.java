package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.ArticleImage;
import com.droptheclothes.api.model.entity.Category;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.repository.ArticleImageRepository;
import com.droptheclothes.api.repository.ArticleRepository;
import com.droptheclothes.api.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class KeepOrDropService {

    private final MemberService memberService;

    private final ObjectStorageService objectStorageService;

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    private final ArticleImageRepository articleImageRepository;

    @Transactional
    public void registerKeepOrDropArticle(KeepOrDropArticleRegisterRequest request, List<MultipartFile> images) {
        // TODO: Member ID 처리
        final String MEMBER_ID = "kakao_2467164020";
        Member member = memberService.getMemberById(MEMBER_ID);

        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 올바르지 않습니다."));

        Article article = Article.of(request, member, category);
        articleRepository.save(article);

        if (!Objects.isNull(images)) {
            List<String> uploadPathAndFileName = storeArticleImages(article, images);
            uploadPathAndFileName.stream().forEach(filepath -> {
                articleImageRepository.save(ArticleImage.of(article, filepath));
            });
        }
    }

    public List<KeepOrDropArticleResponse> getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
        if (!StringUtils.isBlank(request.getCategory())) {
            categoryRepository.findByName(request.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("카테고리 설정이 잘못되었습니다."));
        }

        List<Article> articles = articleRepository.getKeepOrDropArticles(request);
        return KeepOrDropArticleResponse.of(articles);
    }

    public List<String> getItemCategories() {
        return categoryRepository.findByOrderByListOrderAsc().stream()
                .map(category -> category.getName())
                .collect(Collectors.toList());
    }

    private List<String> storeArticleImages(Article article, List<MultipartFile> images) {
        final int MAX_IMAGE_FILE_COUNT = 3;
        if (images.size() > MAX_IMAGE_FILE_COUNT) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드 할 수 있습니다.");
        }

        final String DIRECTORY = "article/" + article.getArticleId().toString();

        List<String> uploadPaths = new ArrayList<>();

        for (MultipartFile image : images) {
            if (Objects.isNull(image)) continue;
            String uploadPath = objectStorageService.uploadFileToObjectStorage(DIRECTORY, image);
            uploadPaths.add(uploadPath);
        }
        return uploadPaths;
    }
}
