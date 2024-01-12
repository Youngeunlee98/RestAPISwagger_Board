package com.example.mybatisboard.impl;

import com.example.mybatisboard.domain.Board;
import com.example.mybatisboard.error.CustomException;
import com.example.mybatisboard.error.ErrorCode;
import com.example.mybatisboard.mapper.BoardMapper;
import com.example.mybatisboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardMapper boardMapper;
    @Override
    public int getTotalCount() {
        return boardMapper.getTotalCount();
    }
    //페이징처리
    @Override
    public List<Board> findAllWithPaging(int offset, int pageSize) {
        return boardMapper.findAllWithPaging(offset, pageSize);
    }


    @Override
    public Board findById(Long id) {
        return boardMapper.findById(id);
    }

    @Override
    public Board save(Board board) {
        boardMapper.save(board);
        return board;
    }

    @Override
    public void update(Board board) {
        boardMapper.update(board);
    }

    @Override
    public void delete(Long id) {
        boardMapper.delete(id);
    }

    //조회수 처리
    @Override
    @Transactional
    public Board getBoard(Long id) {
        Board board = boardMapper.findById(id);
        if (board == null) {
            throw new CustomException(ErrorCode.Board_Not_found);
        }
        boardMapper.increaseCount(id);
        return board;
    }

    @Override
    public List<Board> findByTitleContaining(String keyword) {
        return boardMapper.findByTitleContaining(keyword);
    }

    @Override
    public List<Board> findBycontentContaining(String keyword) {
        return boardMapper.findBycontentContaining(keyword);
    }

    @Override
    public List<Board> findAllDesc() {
        return boardMapper.findAllDesc();
    }

    @Override
    public List<Board> findAllByCountDesc() {
        return boardMapper.findAllByCountDesc();
    }

    @Override
    public List<Board> findAllByCommentDesc() {
        return boardMapper.findAllByCommentDesc();
    }


}
