package com.example.mybatisboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema(description = "��� ���� ����. content �ʵ常 ���˴ϴ�.")
public class CommentUpdateDTO {
    /**
     * ��� ����
     */
    @NotBlank(message = "����� �� ���� �̻� ���� �����ϸ� ���� �� �� ���� ������ �ʽ��ϴ�.")
    @Schema(description = "��� ����", required = true)
    private String content;
}
