package com.example.mybatisboard.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "게시물 리스트 Domain")
@Getter
@Setter
public class Board {
    @Schema(description = "게시글 고유번호")
    private Long id;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "게시글 작성일", example = "yyMMdd")
    private String createdAt;

    @Schema(description = "게시글 수정일", example = "yyMMdd")
    private String updatedAt;

    //조회수
    @Schema(description = "게시글 조회수")
    private Long count = 1L;

    //댓글수
    @Schema(description = "게시글 댓글수")
    private Long comment = 0L;

    @Schema(description = "게시글 작성자 고유 id")
    private Long boardWriter;

    @Schema(description = "게시글 작성자의 유저 아이디(userId)")
    private String boardUsername;

}
