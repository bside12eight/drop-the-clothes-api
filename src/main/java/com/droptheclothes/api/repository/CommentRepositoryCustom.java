package com.droptheclothes.api.repository;

public interface CommentRepositoryCustom {

    Long getCommentListOrder(Long articleId, Long parentId);
}