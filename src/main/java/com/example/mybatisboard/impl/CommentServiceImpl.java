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

    @Override // 게시글에 해당하는 댓글을 찾rl
    public List<Comment> findByBoardId(Long boardId) {
        return commentMapper.findByBoardId(boardId);
    }

    //저장
    @Override
    public Comment save(Comment comment) {
        // writer 필드가 null인 경우, 디폴트값 '관리자'를 설정
        // 날짜 포맷터 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 현재 시간을 LocalDateTime으로 가져오고 이를 String으로 포맷
        String nowAsString = LocalDateTime.now().format(formatter);
        // 댓글 객체의 날짜 필드에 포맷된 문자열을 설정
        comment.setCreatedDate(nowAsString);
        comment.setUpdatedDate(nowAsString);

        commentMapper.save(comment);
        System.out.println(comment);
        increaseCommentCount(comment.getBoardId());

        return comment;
    }

    //삭제
    @Override
    public Long delete(Long id) {
        Long boardId = commentMapper.findBoardIdById(id);

        if (boardId == null) {
            throw new NoSuchElementException("댓글의 id를 찾을 수 없습니다: " + id);
        }

        commentMapper.delete(id);
        decreaseCommentCount(boardId);
        return boardId;
    }

    //수정
    @Override
    public Comment update(Comment comment) {
        Comment existingComment = commentMapper.findByCommentId(comment.getId());
        if (existingComment == null) {
            // 기존 댓글이 존재하지 않는 경우
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

        // 기존 댓글의 boardId를 새로운 comment 객체에 설정
        comment.setBoardId(existingComment.getBoardId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowAsString = LocalDateTime.now().format(formatter);
        comment.setUpdatedDate(nowAsString);
        // 댓글 업데이트
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
