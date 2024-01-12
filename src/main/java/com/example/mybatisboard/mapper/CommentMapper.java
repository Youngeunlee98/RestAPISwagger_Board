package com.example.mybatisboard.mapper;

import com.example.mybatisboard.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("SELECT id, board_id, content, writer, created_date, updated_date, username FROM comment WHERE board_id = #{boardId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "writer", column = "writer"),
            @Result(property = "createdDate", column = "created_date"),
            @Result(property = "updatedDate", column = "updated_date"),
            @Result(property = "username", column = "username")
    })
    List<Comment> findByBoardId(@Param("boardId") Long boardId);


    @Insert("INSERT INTO comment (board_id, content, writer, created_date, updated_date, username) " +
            "VALUES (#{boardId}, #{content}, #{writer}, NOW(), NOW(), #{username})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Comment comment);

    @Update("UPDATE comment SET content = #{content}, updated_date = NOW() WHERE id = #{id}")
    void update(Comment comment);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    void delete(@Param("id") Long id);

    @Select("SELECT board_id FROM comment WHERE id = #{id}")
    Long findBoardIdById(@Param("id") Long id); // 댓글이 속한 게시글의 id

    //댓글의 id값 찾기
    @Select("SELECT * FROM comment WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "boardId", column = "board_id"),
            @Result(property = "content", column = "content"),
            @Result(property = "writer", column = "writer"),
            @Result(property = "createdDate", column = "created_date"),
            @Result(property = "updatedDate", column = "updated_date"),
            @Result(property = "username" , column = "username")
    })
    Comment findByCommentId(@Param("id") Long id);
}