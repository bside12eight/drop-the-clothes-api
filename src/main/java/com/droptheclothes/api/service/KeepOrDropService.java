package com.droptheclothes.api.service;

import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.ArticleCommentResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRegisterRequest;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleResponse;
import com.droptheclothes.api.model.dto.keepordrop.KeepOrDropArticleRetrieveRequest;
import com.droptheclothes.api.model.entity.Article;
import com.droptheclothes.api.model.entity.ArticleImage;
import com.droptheclothes.api.model.entity.Category;
import com.droptheclothes.api.model.entity.Charge;
import com.droptheclothes.api.model.entity.Comment;
import com.droptheclothes.api.model.entity.Member;
import com.droptheclothes.api.model.entity.Vote;
import com.droptheclothes.api.model.enums.ChargeReasonType;
import com.droptheclothes.api.model.enums.VoteType;
import com.droptheclothes.api.repository.ArticleImageRepository;
import com.droptheclothes.api.repository.ArticleRepository;
import com.droptheclothes.api.repository.CategoryRepository;
import com.droptheclothes.api.repository.ChargeRepository;
import com.droptheclothes.api.repository.CommentRepository;
import com.droptheclothes.api.repository.VoteRepository;
import com.droptheclothes.api.security.SecurityUtility;
import com.droptheclothes.api.utility.BusinessConstants;
import com.droptheclothes.api.utility.MessageConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    private final ChargeRepository chargeRepository;

    @Transactional
    public void registerKeepOrDropArticle(KeepOrDropArticleRegisterRequest request, List<MultipartFile> images) {
        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        Category category = categoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.WRONG_REQUEST_PARAMETER_MESSAGE));

        Article article = Article.of(request, member, category);
        articleRepository.save(article);

        if (!Objects.isNull(images)) {
            storeArticleImages(article, images).stream()
                    .forEach(filepath -> articleImageRepository.save(ArticleImage.of(article, filepath)));
        }
    }

    public List<KeepOrDropArticleResponse> getKeepOrDropArticles(KeepOrDropArticleRetrieveRequest request) {
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
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));
        return KeepOrDropArticleResponse.of(article);
    }

    @Transactional
    public void registerArticleComment(Long articleId, ArticleCommentRegisterRequest request) {
        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));
        article.addComment();

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

    @Transactional
    public void deleteArticleComment(Long articleId, Long commentId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));
        article.removeComment();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));
        comment.delete();
    }

    public List<ArticleCommentResponse> getArticleComments(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));

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
        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));

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

    @Transactional
    public void blockKeepOrDropArticle(Long articleId, ChargeReasonType chargeReason) {
        Member member = memberService.getActiveMemberById(SecurityUtility.getMemberId());

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.NO_MATCHDE_CONTENTS_MESSAGE));
        article.addCharge();

        Charge charge = Charge.builder()
                .member(member)
                .article(article)
                .chargeReason(chargeReason)
                .createdAt(LocalDateTime.now())
                .build();
        chargeRepository.save(charge);
    }

    private List<String> storeArticleImages(Article article, List<MultipartFile> images) {
        if (images.size() > BusinessConstants.MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException(String.format(MessageConstants.MAX_IMAGE_COUNT_EXCEED_MESSAGE, BusinessConstants.MAX_IMAGE_COUNT));
        }
        final String DIRECTORY = BusinessConstants.ARTICLE_IMAGE_DIRECTORY + article.getArticleId().toString();

        return images.stream()
                .map(image -> objectStorageService.uploadFileToObjectStorage(DIRECTORY, image))
                .collect(Collectors.toList());
    }
}
