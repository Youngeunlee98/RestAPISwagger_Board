package com.example.mybatisboard.service;

import com.example.mybatisboard.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findByBoardId(Long boardId); //�Խñ��� ��� ã��

    Comment save(Comment comment); //����

    Long delete(Long id); // ����

    Comment update(Comment comment); // ����

    Comment findById(Long id);

    void increaseCommentCount(Long boardId); // �Խñ��� ��� �� ����
    void decreaseCommentCount(Long boardId); // �Խñ��� ��� �� ����
}
