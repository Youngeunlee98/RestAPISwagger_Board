package com.example.mybatisboard.service;

import com.example.mybatisboard.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> findAllWithPaging(int offset, int pageSize);

    Board findById(Long id);

    Board save(Board board);

    void update(Board board);

    void delete(Long id);

    int getTotalCount();

    Board getBoard(Long id);

    List<Board> findByTitleContaining(String keyword);

    List<Board> findBycontentContaining(String keyword);

    // 최신순 정렬
    List<Board> findAllDesc();

    // 조회수 높은순 정렬
    List<Board> findAllByCountDesc();

    // 댓글 많은 순 정렬
    List<Board> findAllByCommentDesc();
}
