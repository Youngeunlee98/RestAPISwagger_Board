package com.example.mybatisboard.mapper;

import com.example.mybatisboard.domain.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {

    @Select("SELECT COUNT(*) FROM board")
    int getTotalCount();

    @Select("SELECT *, ROW_NUMBER() OVER (ORDER BY id DESC) AS rownum " +
            "FROM board ORDER BY id DESC LIMIT #{offset}, #{pageSize}")
    List<Board> findAllWithPaging(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT * FROM board WHERE id = #{id}")
    Board findById(Long id);

    @Insert("INSERT INTO board (title, content, createdAt, updatedAt, boardWriter, boardUsername) " +
            "VALUES (#{title}, #{content}, NOW(), NOW(), #{boardWriter}, #{boardUsername})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(Board board);

    @Update("UPDATE board SET title = #{title}, content = #{content}, updatedAt = NOW() , boardWriter = #{boardWriter}, boardUsername = #{boardUsername} WHERE id = #{id}")
    void update(Board board);

    @Delete("DELETE FROM board WHERE id = #{id}")
    void delete(Long id);

    //조회수증가
    @Update("UPDATE board SET count = count + 1 WHERE id = #{id}")
    void increaseCount(Long id);

    @Select("SELECT * FROM board WHERE title LIKE CONCAT('%', #{keyword}, '%')")
    List<Board> findByTitleContaining(@Param("keyword")String keyword);

    @Select("SELECT * FROM board WHERE content LIKE CONCAT('%', #{keyword}, '%')")
    List<Board> findBycontentContaining(@Param("keyword")String keyword);
    @Select("SELECT * FROM board ORDER BY id DESC")
    List<Board> findAllDesc();

    @Select("SELECT * FROM board ORDER BY count DESC")
    List<Board> findAllByCountDesc();

    @Select("SELECT * FROM board ORDER BY comment DESC")
    List<Board> findAllByCommentDesc();

    // 댓글 수 증가
    @Update("UPDATE board SET comment = comment + 1 WHERE id = #{id}")
    void increaseCommentCount(Long id);

    // 댓글 수 감소
    @Update("UPDATE board SET comment = comment - 1 WHERE id = #{id}")
    void decreaseCommentCount(Long id);
}
