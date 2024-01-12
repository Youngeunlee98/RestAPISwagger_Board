package com.example.mybatisboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema(description = "댓글 수정 정보. content 필드만 사용됩니다.")
public class CommentUpdateDTO {
    /**
     * 댓글 내용
     */
    @NotBlank(message = "댓글을 한 글자 이상 수정 가능하며 수정 시 빈 값은 허용되지 않습니다.")
    @Schema(description = "댓글 내용", required = true)
    private String content;
}
