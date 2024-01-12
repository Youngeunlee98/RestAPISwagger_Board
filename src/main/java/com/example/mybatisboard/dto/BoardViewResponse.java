package com.example.mybatisboard.dto;

import com.example.mybatisboard.domain.Board;
import com.example.mybatisboard.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "글 상세 조회 컨트롤러 200 OK 반환 예시")
public class BoardViewResponse {
    //댓글 목록
    @Schema(description = "댓글 목록")
    private List<Comment> comments;

    //게시글 내용
    @Schema(description = "게시글 내용")
    private Board board;

}
