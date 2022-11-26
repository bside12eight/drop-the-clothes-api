package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.ArticleImage;
import com.droptheclothes.api.model.entity.Category;
import com.droptheclothes.api.model.entity.Comment;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.Vote;
import com.droptheclothes.api.model.enums.VoteType;
import com.droptheclothes.api.repository.ArticleImageRepository;
import com.droptheclothes.api.repository.ArticleRepository;
import com.droptheclothes.api.repository.CategoryRepository;
import com.droptheclothes.api.repository.CommentRepository;
import com.droptheclothes.api.repository.VoteRepository;
import com.droptheclothes.api.security.SecurityUtility;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    private final CommentRepository commentRepository;

    private final VoteRepository voteRepository;

    @Transactional
    public void registerKeepOrDropArticle(KeepOrDropArticleRegisterRequest request, List<MultipartFile> images) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

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

    public KeepOrDropArticleResponse getKeepOrDropArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        return KeepOrDropArticleResponse.of(article);
    }

    @Transactional
    public void registerArticleComment(Long articleId, ArticleCommentRegisterRequest request) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        Comment parentComment = null;
        int listOrder;
        if (!Objects.isNull(request.getParentId())) {
            parentComment = commentRepository.findById(request.getParentId()).orElse(null);
            listOrder = parentComment.getChildren().size();
        } else {
            listOrder = commentRepository.getCommentListOrder(articleId, request.getParentId()).intValue();
        }

        Comment commentEntity = Comment.builder()
                .member(member)
                .article(article)
                .comment(request.getComment())
                .parent(parentComment)
                .listOrder(listOrder + 1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        commentRepository.save(commentEntity);
    }

    public List<ArticleCommentResponse> getArticleComments(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        ArrayList<Comment> comments = new ArrayList<>(article.getComments());
        comments.sort((o1, o2) -> {
            if (o1.getCreatedAt().isEqual(o2.getCreatedAt())) {
                return 0;
            } else if (o1.getCreatedAt().isBefore(o2.getCreatedAt())) {
                return -1;
            } else {
                return 1;
            }
        });

        List<ArticleCommentResponse> parents = new ArrayList<>();
        Map<Long, List<ArticleCommentResponse>> childrenMap = new HashMap<>();

        comments.forEach(comment -> {
            ArticleCommentResponse response = ArticleCommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .nickname(comment.getMember().getNickName())
                    .comment(comment.getComment())
                    .createdAt(comment.getCreatedAt())
                    .build();

            if (Objects.isNull(comment.getParent())) {
                parents.add(response);
            } else {
                Long parentId = comment.getParent().getCommentId();
                List<ArticleCommentResponse> children = childrenMap.getOrDefault(parentId, new ArrayList<>());
                children.add(response);
                childrenMap.put(parentId, children);
            }
        });

        parents.stream().forEach(response -> response.setChildren(childrenMap.get(response.getCommentId())));

        return parents;
    }

    @Transactional
    public void voteKeepOrDrop(Long articleId, VoteType voteType) {
        Member member = memberService.getMemberById(SecurityUtility.getMemberId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다."));

        Optional<Vote> optionalVote = voteRepository.findByMemberAndArticle(member, article);
        if (optionalVote.isEmpty()) {
            Vote vote = Vote.builder()
                    .member(member)
                    .article(article)
                    .voteType(voteType)
                    .createdAt(LocalDateTime.now())
                    .build();
            voteRepository.save(vote);
            article.vote(voteType);
        } else {
            Vote beforeVote = optionalVote.get();
            if (beforeVote.getVoteType().equals(voteType)) {
                article.cancelVote(voteType);
            } else {
                article.cancelVote(voteType.equals(VoteType.KEEP) ? VoteType.DROP : VoteType.KEEP);
                Vote vote = Vote.builder()
                        .member(member)
                        .article(article)
                        .voteType(voteType)
                        .createdAt(LocalDateTime.now())
                        .build();
                voteRepository.save(vote);
                article.vote(voteType);
            }
            voteRepository.delete(beforeVote);
        }
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
