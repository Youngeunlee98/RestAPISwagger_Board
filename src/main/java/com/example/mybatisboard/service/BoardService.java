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

    // �ֽż� ����
    List<Board> findAllDesc();

    // ��ȸ�� ������ ����
    List<Board> findAllByCountDesc();

    // ��� ���� �� ����
    List<Board> findAllByCommentDesc();
}
