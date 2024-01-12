package com.example.mybatisboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "게시글 저장 및 수정. 게시글을 저장하거나 수정할 때 사용합니다.")
public class BoardSaveAndUpdateDTO {
    /**
     * 게시글 제목
     */
    @JsonProperty("title")
    @Schema(description = "게시글 제목", required = true)
    private String title;

    /**
     * 게시글 내용
     */
    @JsonProperty("content")
    @Schema(description = "게시글 내용", required = true)
    private String content;
}
