package com.example.mybatisboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "댓글 정보. 댓글을 생성할 때 사용합니다.")
public class CommentSaveDTO {
    /**
     * 게시글 아이디
     */
    private Long boardId;

    /**
     * 댓글 내용
     */
    @Schema(description = "댓글 내용", required = true)
    private String content;

}
