package com.example.mybatisboard.service;

import com.example.mybatisboard.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findByBoardId(Long boardId); //게시글의 댓글 찾기

    Comment save(Comment comment); //저장

    Long delete(Long id); // 삭제

    Comment update(Comment comment); // 수정

    Comment findById(Long id);

    void increaseCommentCount(Long boardId); // 게시글의 댓글 수 증가
    void decreaseCommentCount(Long boardId); // 게시글의 댓글 수 감소
}
