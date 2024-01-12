package com.example.mybatisboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "�Խñ� ���� �� ����. �Խñ��� �����ϰų� ������ �� ����մϴ�.")
public class BoardSaveAndUpdateDTO {
    /**
     * �Խñ� ����
     */
    @JsonProperty("title")
    @Schema(description = "�Խñ� ����", required = true)
    private String title;

    /**
     * �Խñ� ����
     */
    @JsonProperty("content")
    @Schema(description = "�Խñ� ����", required = true)
    private String content;
}
