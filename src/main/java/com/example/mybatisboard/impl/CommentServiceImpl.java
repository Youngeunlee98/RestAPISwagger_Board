package com.example.mybatisboard.impl;

import com.example.mybatisboard.domain.Comment;
import com.example.mybatisboard.mapper.BoardMapper;
import com.example.mybatisboard.mapper.CommentMapper;
import com.example.mybatisboard.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BoardMapper boardMapper;

    @Override // �Խñۿ� �ش��ϴ� ����� ãrl
    public List<Comment> findByBoardId(Long boardId) {
        return commentMapper.findByBoardId(boardId);
    }

    //����
    @Override
    public Comment save(Comment comment) {
        // writer �ʵ尡 null�� ���, ����Ʈ�� '������'�� ����
        // ��¥ ������ ����
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // ���� �ð��� LocalDateTime���� �������� �̸� String���� ����
        String nowAsString = LocalDateTime.now().format(formatter);
        // ��� ��ü�� ��¥ �ʵ忡 ���˵� ���ڿ��� ����
        comment.setCreatedDate(nowAsString);
        comment.setUpdatedDate(nowAsString);

        commentMapper.save(comment);
        System.out.println(comment);
        increaseCommentCount(comment.getBoardId());

        return comment;
    }

    //����
    @Override
    public Long delete(Long id) {
        Long boardId = commentMapper.findBoardIdById(id);

        if (boardId == null) {
            throw new NoSuchElementException("����� id�� ã�� �� �����ϴ�: " + id);
        }

        commentMapper.delete(id);
        decreaseCommentCount(boardId);
        return boardId;
    }

    //����
    @Override
    public Comment update(Comment comment) {
        Comment existingComment = commentMapper.findByCommentId(comment.getId());
        if (existingComment == null) {
            // ���� ����� �������� �ʴ� ���
            throw new IllegalArgumentException("����� �������� �ʽ��ϴ�.");
        }

        // ���� ����� boardId�� ���ο� comment ��ü�� ����
        comment.setBoardId(existingComment.getBoardId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowAsString = LocalDateTime.now().format(formatter);
        comment.setUpdatedDate(nowAsString);
        // ��� ������Ʈ
        commentMapper.update(comment);
        return comment;
    }
    @Override
    public Comment findById(Long id) {
        return commentMapper.findByCommentId(id);
    }

    @Override
    public void increaseCommentCount(Long boardId) {
        boardMapper.increaseCommentCount(boardId);
    }

    @Override
    public void decreaseCommentCount(Long boardId) {
        boardMapper.decreaseCommentCount(boardId);
    }

}
