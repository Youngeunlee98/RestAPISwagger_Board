package com.example.mybatisboard.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "댓글 리스트 Domain")
@Getter
@Setter
public class Comment {
    @Schema(description = "댓글의 고유 아이디값")
    private Long id;

    @Schema(description = "댓글이 달린 게시글의 아이디")
    private Long boardId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 작성자 고유 id")
    private Long writer;

    @Schema(description = "댓글 작성일자", example = "yyMMdd")
    private String createdDate;

    @Schema(description = "댓글 수정일자", example = "yyMMdd")
    private String updatedDate;

    @Schema(description = "댓글 작성자의 유저 아이디(userId)")
    private String username;
}
